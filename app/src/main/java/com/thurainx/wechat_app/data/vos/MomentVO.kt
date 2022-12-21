package com.thurainx.wechat_app.data.vos

data class MomentVO(
    val text: String,
    val millis: Long,
    val photoList: List<String>,
    val videoLink: String,
    val name: String,
    val phone: String,
    val profileImage: String
)