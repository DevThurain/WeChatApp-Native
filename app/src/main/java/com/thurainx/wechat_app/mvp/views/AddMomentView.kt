package com.thurainx.wechat_app.mvp.views

interface AddMomentView : BasedView {
    fun navigateBack()
    fun pickFiles()
    fun onBindUserData(name: String, profileImage: String)
    fun showLoadingDialog()
    fun dismissLoadingDialog()
}