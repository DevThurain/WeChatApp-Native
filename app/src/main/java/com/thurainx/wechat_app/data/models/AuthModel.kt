package com.thurainx.wechat_app.data.models

import com.thurainx.wechat_app.network.auth.AuthManager

interface AuthModel {
    var mAuthManager: AuthManager

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

    fun logoutUser()

    fun updateUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

}