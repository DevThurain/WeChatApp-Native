package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.delegate.ContactDelegate
import com.thurainx.wechat_app.delegate.ContactSelectDelegate
import com.thurainx.wechat_app.delegate.GroupDelegate
import com.thurainx.wechat_app.mvp.views.ContactView

interface ContactPresenter : BasedPresenter<ContactView>, ContactDelegate, ContactSelectDelegate, GroupDelegate {
    fun onTapAddContact()
    fun onTapCreateGroup()
    fun refreshGroupList()
    fun addContact(friendId: String)
}