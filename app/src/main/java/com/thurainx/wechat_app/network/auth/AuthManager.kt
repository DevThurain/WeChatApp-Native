package com.thurainx.wechat_app.network.auth

interface AuthManager {
    fun registerUser(name: String, password: String, dob: String, gender: String)
}