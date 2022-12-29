package com.thurainx.wechat_app.data.vos

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ContactVO(
    val id: String = "",
    val name: String = "",
    val photoUrl: String = "",
    var lastMessage: String = "",
    var isGroup: Boolean = false
)