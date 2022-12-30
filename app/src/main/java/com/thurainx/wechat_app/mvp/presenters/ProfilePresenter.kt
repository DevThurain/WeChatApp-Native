package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.delegate.EditDialogDelegate
import com.thurainx.wechat_app.delegate.MomentDelegate
import com.thurainx.wechat_app.mvp.views.ProfileView

interface ProfilePresenter: BasedPresenter<ProfileView>, MomentDelegate, EditDialogDelegate {
    fun onTapQr()
    fun onTapEditProfile()
}