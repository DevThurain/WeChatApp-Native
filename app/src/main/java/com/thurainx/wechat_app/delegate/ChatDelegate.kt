package com.thurainx.wechat_app.delegate

import com.thurainx.wechat_app.data.vos.ContactVO

interface ChatDelegate {
    fun onTapChat(contactVO: ContactVO)
}