package com.thurainx.wechat_app.network.cloud_firestore

interface CloudFireStoreApi {
    fun registerUser(name: String, dob: String, gender: String,onSuccess: () -> Unit, onFailure: (String) -> Unit)

}