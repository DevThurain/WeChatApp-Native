package com.thurainx.wechat_app.network.auth

import com.google.firebase.auth.FirebaseAuth

object AuthManagerImpl: AuthManager {
    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun registerUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val email = phone.plus("@gmail.com")
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful && it.isComplete){
                val userId = it.result?.user?.uid ?: ""
                onSuccess(userId)
            } else {
                onFailure(it.exception?.message ?: "Please Check Internet Connection")
            }
        }

    }

    override fun loginUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val email = phone.plus("@gmail.com")
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful && it.isComplete){
                val userId = it.result?.user?.uid ?: ""
                onSuccess(userId)
            } else {
                onFailure(it.exception?.message ?: "Please Check Internet Connection")
            }
        }

    }

}