package com.thurainx.wechat_app.mvp.presenters

import android.graphics.Bitmap
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.delegate.ContactDelegate
import com.thurainx.wechat_app.delegate.ContactSelectDelegate
import com.thurainx.wechat_app.mvp.views.CreateGroupView

interface CreateGroupPresenter : BasedPresenter<CreateGroupView>, ContactDelegate, ContactSelectDelegate {
    fun pickGroupImage()
    fun onTapBack()
    fun createGroup(name: String, bitmap: Bitmap)
}