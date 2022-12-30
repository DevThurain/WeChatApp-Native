package com.thurainx.wechat_app.network.auth

interface AuthManager {
    fun registerUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun loginUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun updateUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )


    fun logoutUser()
}