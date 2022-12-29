package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.GroupVO

interface ContactView : BasedView {
    fun navigateToQrScannerScreen()
    fun navigateToCreateGroupScreen()
    fun bindContacts(contactList: List<ContactVO>)
    fun bindGroups(groupList: List<GroupVO>)
    fun navigateToGroupChatScreen(groupVO: GroupVO)
    fun navigateToChatRoomScreen(contactVO: ContactVO)
}