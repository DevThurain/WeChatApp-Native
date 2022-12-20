package com.thurainx.wechat_app.data.models

import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApi
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApi

interface WeChatModel {
    var mCloudFireStoreApi: CloudFireStoreApi
    var mRealTimeDatabaseApi: RealTimeDatabaseApi

    fun registerUser(
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun loginUser(
        phone: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

}