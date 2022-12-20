package com.thurainx.wechat_app.network.cloud_firestore

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.thurainx.wechat_app.utils.*
import java.io.ByteArrayOutputStream
import java.util.*

object CloudFireStoreApiImpl : CloudFireStoreApi {

    val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference

    override fun registerUser(
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
                    insertUser(name, phone, password, dob, gender, onSuccess, onFailure)
                } else {
                    onFailure("User already exist.")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Please check connection")
            }
    }

    override fun loginUser(
        phone: String,
        password: String,
        onSuccess: (name: String, phone: String, dob: String, gender: String, profileImage: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(phone)
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
        phone: String,
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
                val imageUrl = task.result?.toString()
                updateProfile(phone, imageUrl.toString(), onSuccess, onFailure)
            }
        }.run {
            updateProfile(phone, DEFAULT_PROFILE_IMAGE, onSuccess, onFailure)
        }
    }


    private fun insertUser(
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userMap = hashMapOf(
            FIRE_STORE_REF_NAME to name,
            FIRE_STORE_REF_PHONE to phone,
            FIRE_STORE_REF_PASSWORD to password,
            FIRE_STORE_REF_DOB to dob,
            FIRE_STORE_REF_GENDER to gender,
            FIRE_STORE_REF_PROFILE_IMAGE to ""
        )

        db.collection("users")
            .document(phone)
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
        phone: String,
        profileImage: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(phone)
            .update("profileImage", profileImage)
            .addOnSuccessListener {
                onSuccess(profileImage)
            }.addOnFailureListener {
                onFailure(it.message ?: "Update to fire store failed.")
            }
    }


}