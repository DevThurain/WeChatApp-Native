package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.mvp.views.ProfileView

interface ProfilePresenter: BasedPresenter<ProfileView> {
    fun onTapQr()
    fun onTapEditProfile()
}