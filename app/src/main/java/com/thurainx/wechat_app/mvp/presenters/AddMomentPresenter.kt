package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.mvp.views.AddMomentView
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.MomentView

interface AddMomentPresenter : BasedPresenter<AddMomentView> {
    fun onTapPickFile()
}