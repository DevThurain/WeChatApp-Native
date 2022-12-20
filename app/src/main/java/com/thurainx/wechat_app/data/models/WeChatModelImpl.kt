package com.thurainx.wechat_app.data.models

import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApi
import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApiImpl
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApi
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApiImpl

object WeChatModelImpl : WeChatModel{
    override var mCloudFireStoreApi: CloudFireStoreApi = CloudFireStoreApiImpl
    override var mRealTimeDatabaseApi: RealTimeDatabaseApi = RealTimeDatabaseApiImpl


    override fun registerUser(
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.registerUser(name, phone, password, dob, gender, onSuccess, onFailure)
    }

    override fun loginUser(
        phone: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.loginUser(phone, password, onSuccess, onFailure)
    }

}