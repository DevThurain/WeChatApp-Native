package com.thurainx.wechat_app.network.cloud_firestore

import com.google.firebase.auth.FirebaseAuth

object CloudFireStoreApiImpl : CloudFireStoreApi {

    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun registerUser(
        name: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
//        mFirebaseAuth.signw(email, password).addOnCompleteListener {
//            if(it.isSuccessful && it.isComplete){
//                onSuccess()
//            } else {
//                onFailure(it.exception?.message ?: "Please Check Internet Connection")
//            }
//        }
    }


}