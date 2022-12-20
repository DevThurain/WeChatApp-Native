package com.thurainx.wechat_app.network.cloud_firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object CloudFireStoreApiImpl : CloudFireStoreApi {

    val db = Firebase.firestore

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
                if(!result.exists()){
                    insertUser(name, phone, password, dob, gender,onSuccess, onFailure)
                }else{
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
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("users")
            .document(phone)
            .get()
            .addOnSuccessListener { result ->
                if(result.exists()){
                     if(password == result.get("password")){
                         onSuccess()
                         Log.d("Success","Login Success")
                     }else{
                         onFailure("Incorrect password.")
                     }
                }else{
                    onFailure("User does not exist.")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Please check connection")
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
    ){
        val userMap = hashMapOf(
            "name" to name,
            "phone" to phone,
            "password" to password,
            "dob" to dob,
            "gender" to gender,
        )

        db.collection("users")
            .document(phone)
            .set(userMap)
            .addOnSuccessListener {
                onSuccess()
                Log.d("Success", "Successfully added user") }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to add user to fire store.")
                Log.d("Failure", "Failed to add user") }
    }




}