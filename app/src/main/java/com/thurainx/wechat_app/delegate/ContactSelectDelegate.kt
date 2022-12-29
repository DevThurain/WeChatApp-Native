package com.thurainx.wechat_app.delegate

import com.thurainx.wechat_app.data.vos.ContactVO

interface ContactSelectDelegate {
    fun onSelectContact(isSelect: Boolean, contactVO: ContactVO)
}