package com.thurainx.wechat_app.data.vos

data class MessageVO(
    val text: String,
    val millis: Long,
    val photoList: List<String>,
    val videoLink: String,
    val name: String,
    val id: String,
    val profileImage: String,
)
