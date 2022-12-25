package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.ContactVO

interface ContactView : BasedView {
    fun navigateToQrScannerScreen()
    fun bindContacts(contactList: List<ContactVO>)
}