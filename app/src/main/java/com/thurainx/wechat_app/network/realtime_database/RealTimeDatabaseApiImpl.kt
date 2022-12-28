package com.thurainx.wechat_app.network.realtime_database

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MessageVO
import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApiImpl
import com.thurainx.wechat_app.utils.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

object RealTimeDatabaseApiImpl : RealTimeDatabaseApi {
    private val database: DatabaseReference = Firebase.database.reference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference


    override fun addMessage(
        otherId: String,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {


        val uploadedLinkList: ArrayList<String> = arrayListOf()

        if (fileList.isNotEmpty()) {
            uploadMultiFile(
                fileList = fileList,
                onSuccess = {
                    uploadedLinkList.add(it)
                    Log.d("multi_file_link", it)
                    if (uploadedLinkList.size == fileList.size) {
                        insertMessages(
                            otherId = otherId,
                            uploadedLinkList = uploadedLinkList,
                            isMovie = fileList.first().realPath.isNotEmpty(),
                            messageVO = messageVO,
                            onSuccess, onFailure
                        )
                    }
                },
                onFailure = {
                    onFailure(it)
                }
            )
        } else {
            insertMessages(
                otherId = otherId,
                uploadedLinkList = uploadedLinkList,
                isMovie = false,
                messageVO = messageVO,
                onSuccess, onFailure
            )
        }
    }

    override fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        database.child("contactsAndMessages")
            .child(ownId)
            .child(otherId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFail(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList = arrayListOf<MessageVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        dataSnapShot.getValue(MessageVO::class.java)?.let {
                            messageList.add(it)
                        }
//                        val message = MessageVO(
//                            text =
//                        )

                        Log.d("firebase", dataSnapShot.toString())
                    }
                    onSuccess(messageList)
                }
            })
    }


    private fun uploadMultiFile(
        fileList: List<FileVO>,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {

        fileList.forEach {
            if (!it.isMovie) {
                val baos = ByteArrayOutputStream()
                it.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val imageRef = storageReference.child("files/${UUID.randomUUID()}")
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    onFailure(it.message ?: "Upload image to firebase storage failed.")
                }.addOnSuccessListener { taskSnapshot ->

                }

                val urlTask = uploadTask.continueWithTask {
                    return@continueWithTask imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    val imageUrl = task.result?.toString()
                    onSuccess(imageUrl.toString())
                }
            } else {
                var file = Uri.fromFile(File(it.realPath))
                val videoRef = storageReference.child("videos/${file.lastPathSegment}")

                val uploadTask = videoRef.putFile(file)
                uploadTask.addOnFailureListener {
                    onFailure(it.message ?: "Upload video to firebase storage failed.")
                }.addOnSuccessListener { taskSnapshot ->

                }

                val urlTask = uploadTask.continueWithTask {
                    return@continueWithTask videoRef.downloadUrl
                }.addOnCompleteListener { task ->
                    val imageUrl = task.result?.toString()
                    onSuccess(imageUrl.toString())
                }


            }

        }
    }


    private fun insertMessages(
        otherId: String,
        uploadedLinkList: List<String>,
        isMovie: Boolean,
        messageVO: MessageVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {

        var photoList: List<String> = listOf()
        var movieLink: String = ""

        if (isMovie) {
            movieLink = uploadedLinkList.firstOrNull() ?: ""
        } else {
            photoList = uploadedLinkList
        }

        val message = messageVO.copy(
            videoLink = movieLink,
            photoList = ArrayList(photoList)
        )

        Log.d("message", message.toString())

        var ownMessageSuccess = false
        var otherMessageSuccess = false

        database.child("contactsAndMessages").child(messageVO.id)
            .child(otherId)
            .child(message.millis.toString())
            .setValue(message)
            .addOnCompleteListener {
                ownMessageSuccess = true
                if (ownMessageSuccess && otherMessageSuccess) {
                    onSuccess()
                }

            }
            .addOnFailureListener {
                onFailure(it.message ?: "message insert error.")
                Log.d("firebase", it.message ?: "unknown")
            }

        database.child("contactsAndMessages").child(otherId)
            .child(message.id)
            .child(message.millis.toString())
            .setValue(message)
            .addOnCompleteListener {
                otherMessageSuccess = true
                if (ownMessageSuccess && otherMessageSuccess) {
                    onSuccess()
                }
            }
            .addOnFailureListener {
                onFailure(it.message ?: "message insert error.")
                Log.d("firebase", it.message ?: "unknown")
            }


    }


}