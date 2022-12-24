package com.thurainx.wechat_app.data.models

import com.thurainx.wechat_app.network.auth.AuthManager
import com.thurainx.wechat_app.network.auth.AuthManagerImpl

object AuthModelImpl : AuthModel {
    override var mAuthManager: AuthManager = AuthManagerImpl

    override fun registerUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuthManager.registerUser(phone, password, onSuccess, onFailure)
    }

    override fun loginUser(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuthManager.loginUser(phone, password, onSuccess, onFailure)
    }
}