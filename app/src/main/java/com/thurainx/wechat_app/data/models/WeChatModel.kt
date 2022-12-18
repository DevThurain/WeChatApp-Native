package com.thurainx.wechat_app.data.models

import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApi
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApi

interface WeChatModel {
    var mCloudFireStoreApi: CloudFireStoreApi
    var mRealTimeDatabaseApi: RealTimeDatabaseApi

}