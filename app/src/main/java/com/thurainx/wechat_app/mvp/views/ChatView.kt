package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.ContactVO

interface ChatView : BasedView {
    fun bindContacts(contactList: List<ContactVO>)
    fun bindLastMessage(contactList: List<ContactVO>)
    fun navigateToChatRoomScreen(contactVO: ContactVO)
}