package com.thurainx.wechat_app.data.vos

data class GroupVO(
    val name: String,
    val photo: String,
    val members: List<ContactVO>,
    val messages: List<MessageVO>
)
