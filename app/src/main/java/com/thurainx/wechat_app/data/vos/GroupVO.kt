package com.thurainx.wechat_app.data.vos

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GroupVO(
    val id: String = "",
    val name: String = "",
    val photo: String = "",
    val members: List<ContactVO> = listOf(),
    val messages: Map<String, Any> = mapOf()
){
    constructor(): this("","", "", listOf(), mapOf())
}
