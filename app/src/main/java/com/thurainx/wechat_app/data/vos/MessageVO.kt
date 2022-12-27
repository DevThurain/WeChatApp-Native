package com.thurainx.wechat_app.data.vos
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageVO(
    val text: String = "",
    val millis: Long = 0,
    val photoList: List<String> = listOf(),
    val videoLink: String = "",
    val name: String = "",
    val id: String = "",
    val profileImage: String = "",
){}
