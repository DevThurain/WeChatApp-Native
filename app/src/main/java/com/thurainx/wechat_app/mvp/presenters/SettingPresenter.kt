package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.mvp.views.SettingView

interface SettingPresenter : BasedPresenter<SettingView> {
    fun onTapLogout()
}