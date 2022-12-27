package com.thurainx.wechat_app.delegate

import com.thurainx.wechat_app.data.vos.ContactVO

interface ContactDelegate {
    fun onTapContact(contactVO: ContactVO)
}