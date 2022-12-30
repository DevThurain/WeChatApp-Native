package com.thurainx.wechat_app.network.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
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

    override fun updateUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val user = mFirebaseAuth.currentUser
        val newEmail = phone.plus("@gmail.com")

        val authCredential = EmailAuthProvider.getCredential(user?.email.toString(),password)
        user?.reauthenticate(authCredential)
            ?.addOnCompleteListener {
            if(it.isSuccessful && it.isComplete){
                user.updateEmail(newEmail)
                    .addOnCompleteListener {
                        onSuccess(newEmail)
                    }.addOnFailureListener { error ->
                        onFailure(error.message ?: "unable to change phone number")
                    }
            } else {
                onFailure(it.exception?.message ?: "Please Check Internet Connection")
            }
        }
    }

    override fun logoutUser() {
        mFirebaseAuth.signOut()
    }

}