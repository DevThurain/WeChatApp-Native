package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.mvp.views.LoginView

interface LoginPresenter : BasedPresenter<LoginView> {
    fun onTapLogin(phone: String,password: String)
}