package com.thurainx.wechat_app.network.cloud_firestore

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION_CODES.S
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MomentVO
import com.thurainx.wechat_app.utils.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


object CloudFireStoreApiImpl : CloudFireStoreApi {

    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    override fun registerUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        db.collection("users")
            .document(phone)
            .get()
            .addOnSuccessListener { result ->
                if (!result.exists()) {
                    insertUser(id, name, phone, password, dob, gender, onSuccess, onFailure)
                } else {
                    onFailure("User already exist.")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Please check connection")
            }
    }

    override fun loginUser(
        id: String,
        password: String,
        onSuccess: (name: String, phone: String, dob: String, gender: String, profileImage: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    if (password == result.get("password")) {
                        onSuccess(
                            result.get(FIRE_STORE_REF_NAME).toString(),
                            result.get(FIRE_STORE_REF_PHONE).toString(),
                            result.get(FIRE_STORE_REF_DOB).toString(),
                            result.get(FIRE_STORE_REF_GENDER).toString(),
                            result.get(FIRE_STORE_REF_PROFILE_IMAGE).toString()
                        )
                        Log.d("Success", "Login Success")
                    } else {
                        onFailure("Incorrect password.")
                    }
                } else {
                    onFailure("User does not exist.")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Please check connection")
            }
    }

    override fun uploadProfilePicture(
        id: String,
        bitmap: Bitmap?,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        bitmap?.let {
            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val imageRef = storageReference.child("profiles/${UUID.randomUUID()}")
            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                onFailure(it.message ?: "Upload to firebase storage failed.")
            }.addOnSuccessListener { taskSnapshot ->

            }

            val urlTask = uploadTask.continueWithTask {
                return@continueWithTask imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                Log.d("navigation", "upload user choice profile")

                val imageUrl = task.result?.toString()
                updateProfile(id, imageUrl.toString(), onSuccess, onFailure)
            }
        }

        if (bitmap == null) {
            Log.d("navigation", "upload default profile")

            updateProfile(id, DEFAULT_PROFILE_IMAGE, onSuccess, onFailure)
        }
    }

    override fun uploadMoment(
        text: String,
        fileList: List<FileVO>,
        id: String,
        name: String,
        profileImage: String,
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
                        insertMoment(
                            text,
                            uploadedLinkList,
                            fileList.first().realPath.isNotEmpty(),
                            name, id, profileImage, onSuccess, onFailure
                        )
                    }
                },
                onFailure = {
                    onFailure(it)
                }
            )
        } else {
            insertMoment(
                text,
                uploadedLinkList,
                false,
                name, id, profileImage, onSuccess, onFailure
            )
        }


    }

    override fun getMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        db.collectionGroup(FIRE_STORE_REF_BOOK_MARK)
            .whereEqualTo(FIRE_STORE_REF_ID, id)
            .get()
            .addOnCompleteListener {
                val bmDocuments = it.result?.documents ?: listOf()
                val bookMarkMoments: ArrayList<String> = arrayListOf()

                bmDocuments.forEach { document ->
                    val data = document.data
                    bookMarkMoments.add(data?.get(FIRE_STORE_REF_MILLIS) as String)
                }
                Log.d("moments", bookMarkMoments.toString())

                db.collectionGroup(FIRE_STORE_REF_LIKE)
                    .whereEqualTo(FIRE_STORE_REF_ID, id)
                    .get()
                    .addOnCompleteListener {
                        val documents = it.result?.documents ?: listOf()
                        val likeMoments: ArrayList<String> = arrayListOf()

                        documents.forEach { document ->
                            val data = document.data
                            likeMoments.add(data?.get(FIRE_STORE_REF_MILLIS) as String)
                        }
                        Log.d("moments", likeMoments.toString())

                        db.collection("moments")
                            .get()
                            .addOnCompleteListener {

                                val momentList: MutableList<MomentVO> = arrayListOf()

                                val result = it.result?.documents ?: arrayListOf()

                                for (document in result) {
                                    val data = document.data
                                    val moment = MomentVO(
                                        text = data?.get(FIRE_STORE_REF_TEXT) as String,
                                        millis = data[FIRE_STORE_REF_MILLIS] as Long,
                                        name = data[FIRE_STORE_REF_NAME] as String,
                                        profileImage = data[FIRE_STORE_REF_PROFILE_IMAGE] as String,
                                        id = data[FIRE_STORE_REF_ID] as String,
                                        photoList = data[FIRE_STORE_REF_PHOTO_LIST] as List<String>,
                                        videoLink = data[FIRE_STORE_REF_VIDEO_LINK] as String,
                                        isLike = likeMoments.contains(data[FIRE_STORE_REF_MILLIS].toString()),
                                        totalLike = Math.toIntExact(
                                            (data[FIRE_STORE_REF_LIKE_COUNT] ?: 0L) as Long
                                        ),
                                        isBookmark = bookMarkMoments.contains(data[FIRE_STORE_REF_MILLIS].toString())
                                    )
                                    momentList.add(moment)
                                }
                                onSuccess(momentList.reversed())
                            }.addOnFailureListener { error ->
                                onFailure(error.message ?: "moments fetch failed.")
                            }
                    }


            }




    }

    override fun getBookMarkMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collectionGroup(FIRE_STORE_REF_BOOK_MARK)
            .whereEqualTo(FIRE_STORE_REF_ID, id)
            .get()
            .addOnCompleteListener {
                val bmDocuments = it.result?.documents ?: listOf()
                val bookMarkMoments: ArrayList<String> = arrayListOf()

                bmDocuments.forEach { document ->
                    val data = document.data
                    bookMarkMoments.add(data?.get(FIRE_STORE_REF_MILLIS) as String)
                }
                Log.d("moments", bookMarkMoments.toString())

                db.collectionGroup(FIRE_STORE_REF_LIKE)
                    .whereEqualTo(FIRE_STORE_REF_ID, id)
                    .get()
                    .addOnCompleteListener {
                        val documents = it.result?.documents ?: listOf()
                        val likeMoments: ArrayList<String> = arrayListOf()

                        documents.forEach { document ->
                            val data = document.data
                            likeMoments.add(data?.get(FIRE_STORE_REF_MILLIS) as String)
                        }
                        Log.d("moments", likeMoments.toString())

                        db.collection("moments")
                            .get()
                            .addOnCompleteListener {

                                val momentList: MutableList<MomentVO> = arrayListOf()

                                val result = it.result?.documents ?: arrayListOf()

                                for (document in result) {
                                    val data = document.data
                                    val moment = MomentVO(
                                        text = data?.get(FIRE_STORE_REF_TEXT) as String,
                                        millis = data[FIRE_STORE_REF_MILLIS] as Long,
                                        name = data[FIRE_STORE_REF_NAME] as String,
                                        profileImage = data[FIRE_STORE_REF_PROFILE_IMAGE] as String,
                                        id = data[FIRE_STORE_REF_ID] as String,
                                        photoList = data[FIRE_STORE_REF_PHOTO_LIST] as List<String>,
                                        videoLink = data[FIRE_STORE_REF_VIDEO_LINK] as String,
                                        isLike = likeMoments.contains(data[FIRE_STORE_REF_MILLIS].toString()),
                                        totalLike = Math.toIntExact(
                                            (data[FIRE_STORE_REF_LIKE_COUNT] ?: 0L) as Long
                                        ),
                                        isBookmark = bookMarkMoments.contains(data[FIRE_STORE_REF_MILLIS].toString())
                                    )
                                    if(moment.isBookmark){
                                        momentList.add(moment)
                                    }
                                }
                                onSuccess(momentList.reversed())
                            }.addOnFailureListener { error ->
                                onFailure(error.message ?: "moments fetch failed.")
                            }
                    }


            }




    }

    override fun likeMoment(
        like: Boolean,
        momentMillis: String,
        id: String,
        totalLike: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = mapOf<String, Any>(
            FIRE_STORE_REF_MILLIS to momentMillis,
            FIRE_STORE_REF_ID to id,
        )

        if (like) {
            db.collection("moments")
                .document(momentMillis)
                .collection(FIRE_STORE_REF_LIKE)
                .document(id)
                .set(userMap)
                .addOnCompleteListener {
                    updateLikeCount(totalLike + 1, momentMillis, onSuccess, onFailure)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "like reaction failed.")
                }
        } else {
            db.collection("moments")
                .document(momentMillis)
                .collection(FIRE_STORE_REF_LIKE)
                .document(id)
                .delete()
                .addOnCompleteListener {
                    updateLikeCount(totalLike - 1, momentMillis, onSuccess, onFailure)
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "like reaction failed.")
                }
        }


    }

    override fun bookMarkMoment(
        isBookMark: Boolean,
        momentMillis: String,
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = mapOf<String, Any>(
            FIRE_STORE_REF_MILLIS to momentMillis,
            FIRE_STORE_REF_ID to id,
        )

        if (isBookMark) {
            db.collection("moments")
                .document(momentMillis)
                .collection(FIRE_STORE_REF_BOOK_MARK)
                .document(id)
                .set(userMap)
                .addOnCompleteListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "bookmark failed.")
                }
        } else {
            db.collection("moments")
                .document(momentMillis)
                .collection(FIRE_STORE_REF_BOOK_MARK)
                .document(id)
                .delete()
                .addOnCompleteListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(it.message ?: "bookmark failed.")
                }
        }


    }

    override fun addContacts(
        selfId: String,
        friendId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        var selfInsertSuccess = false
        var friendInsertSuccess = false

        db.collection("users")
            .document(friendId)
            .get()
            .addOnCompleteListener {
                val map = it.result.data
                insertUserToContact(
                    selfId = selfId,
                    contactId = friendId,
                    contactName = map?.get(FIRE_STORE_REF_NAME) as String,
                    contactPhone = map[FIRE_STORE_REF_PHONE] as String,
                    contactDob = map[FIRE_STORE_REF_DOB] as String,
                    contactGender = map[FIRE_STORE_REF_GENDER] as String,
                    contactProfile = map[FIRE_STORE_REF_PROFILE_IMAGE] as String,
                    onSuccess = {
                        selfInsertSuccess = true
                        if (selfInsertSuccess && friendInsertSuccess) {
                            onSuccess()
                        }
                    }, onFailure
                )
            }.addOnFailureListener {
                onFailure(it.message ?: "self insert failed.")
            }

        db.collection("users")
            .document(selfId)
            .get()
            .addOnCompleteListener {
                val map = it.result.data
                insertUserToContact(
                    selfId = friendId,
                    contactId = selfId,
                    contactName = map?.get(FIRE_STORE_REF_NAME) as String,
                    contactPhone = map[FIRE_STORE_REF_PHONE] as String,
                    contactDob = map[FIRE_STORE_REF_DOB] as String,
                    contactGender = map[FIRE_STORE_REF_GENDER] as String,
                    contactProfile = map[FIRE_STORE_REF_PROFILE_IMAGE] as String,
                    onSuccess = {
                        friendInsertSuccess = true
                        if (selfInsertSuccess && friendInsertSuccess) {
                            onSuccess()
                        }
                    }, onFailure
                )
            }.addOnFailureListener {
                onFailure(it.message ?: "self insert failed.")
            }
    }

    override fun getContacts(
        id: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(id)
            .collection("contacts")
            .get()
            .addOnCompleteListener {
                val documents = it.result.documents ?: listOf()
                val contactList = arrayListOf<ContactVO>()

                documents.forEach {
                    val data = it.data
                    contactList.add(
                        ContactVO(
                            id = data?.get(FIRE_STORE_REF_ID) as String,
                            name = data[FIRE_STORE_REF_NAME] as String,
                            photoUrl = data[FIRE_STORE_REF_PROFILE_IMAGE] as String
                        )
                    )
                }
                onSuccess(contactList)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "fail to get contacts.")
            }
    }

    private fun updateLikeCount(
        totalLike: Int,
        momentMillis: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("moments")
            .document(momentMillis)
            .update(FIRE_STORE_REF_LIKE_COUNT, totalLike)
            .addOnCompleteListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure(it.message ?: "like count update failed")
            }
    }


    private fun insertUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = hashMapOf(
            FIRE_STORE_REF_ID to id,
            FIRE_STORE_REF_NAME to name,
            FIRE_STORE_REF_PHONE to phone,
            FIRE_STORE_REF_PASSWORD to password,
            FIRE_STORE_REF_DOB to dob,
            FIRE_STORE_REF_GENDER to gender,
            FIRE_STORE_REF_PROFILE_IMAGE to ""
        )

        db.collection("users")
            .document(id)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Success", "Successfully added user")
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to add user to fire store.")
                Log.d("Failure", "Failed to add user")
            }
    }

    private fun insertUserToContact(
        selfId: String,
        contactId: String,
        contactName: String,
        contactPhone: String,
        contactDob: String,
        contactGender: String,
        contactProfile: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = hashMapOf(
            FIRE_STORE_REF_ID to contactId,
            FIRE_STORE_REF_NAME to contactName,
            FIRE_STORE_REF_PHONE to contactPhone,
            FIRE_STORE_REF_DOB to contactDob,
            FIRE_STORE_REF_GENDER to contactGender,
            FIRE_STORE_REF_PROFILE_IMAGE to contactProfile
        )

        db.collection("users")
            .document(selfId)
            .collection("contacts")
            .document(contactId)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Success", "Successfully added user")
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to add user to fire store.")
                Log.d("Failure", "Failed to add user")
            }
    }

    private fun updateProfile(
        id: String,
        profileImage: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(id)
            .update("profileImage", profileImage)
            .addOnSuccessListener {
                Log.d("navigation", "profile updated")
                onSuccess(profileImage)
            }.addOnFailureListener {
                onFailure(it.message ?: "Update to fire store failed.")
            }
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

    private fun insertMoment(
        text: String,
        uploadedLinkList: List<String>,
        isMovie: Boolean,
        name: String,
        id: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        var photoList: List<String> = listOf()
        var movieLink: String = ""
        val millis = System.currentTimeMillis()

        if (isMovie) {
            movieLink = uploadedLinkList.firstOrNull() ?: ""
        } else {
            photoList = uploadedLinkList
        }
        val momentMap = hashMapOf(
            FIRE_STORE_REF_MILLIS to millis,
            FIRE_STORE_REF_TEXT to text,
            FIRE_STORE_REF_PHOTO_LIST to uploadedLinkList,
            FIRE_STORE_REF_VIDEO_LINK to movieLink,
            FIRE_STORE_REF_PHOTO_LIST to photoList,
            FIRE_STORE_REF_NAME to name,
            FIRE_STORE_REF_ID to id,
            FIRE_STORE_REF_PROFILE_IMAGE to profileImage
        )

        db.collection("moments")
            .document(millis.toString())
            .set(momentMap)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Success", "Successfully added moment")
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to add moment to fire store.")
                Log.d("Failure", "Failed to add moment")
            }

    }


}