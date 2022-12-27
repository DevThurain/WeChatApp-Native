package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.delegate.ContactDelegate
import com.thurainx.wechat_app.mvp.views.ContactView

interface ContactPresenter : BasedPresenter<ContactView>, ContactDelegate {
    fun onTapAddContact()
    fun addContact(friendId: String)
}