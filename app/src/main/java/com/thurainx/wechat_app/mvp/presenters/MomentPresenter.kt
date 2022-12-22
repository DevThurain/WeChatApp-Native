package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.delegate.MomentDelegate
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.MomentView

interface MomentPresenter : BasedPresenter<MomentView>, MomentDelegate {
    fun onTapAddMoment()
    fun onRefreshMoment()
}