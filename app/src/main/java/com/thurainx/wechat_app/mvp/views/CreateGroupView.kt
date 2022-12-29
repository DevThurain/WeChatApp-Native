package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.ContactVO

interface CreateGroupView : BasedView {
    fun bindContacts(contactList: List<ContactVO>)
    fun pickGroupImage()
    fun createGroupSuccess()
    fun navigateBack()
}