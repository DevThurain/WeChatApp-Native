package com.thurainx.wechat_app.data.vos

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GroupVO(
    val name: String? = "",
    val photo: String? = "",
    val members: List<ContactVO>? = listOf(),
    val messages: List<MessageVO>? = listOf()
){
    constructor(): this("", "", listOf(), listOf())
}
