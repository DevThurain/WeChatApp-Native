package com.thurainx.wechat_app.mvp.views

interface AddMomentView : BasedView {
    fun pickFiles()
    fun onBindUserData(name: String, profileImage: String)

}