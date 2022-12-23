package com.thurainx.wechat_app.data.vos

import com.thurainx.wechat_app.utils.CONTENT

data class ContentVO(
    val content: CONTENT,
    val image: Int,
    var isSelected: Boolean = false
)

