package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.mvp.views.OtpView

interface OtpPresenter: BasedPresenter<OtpView> {
    fun onTapVerify(name: String,password: String, phone: String, dob: String, gender: String)

}