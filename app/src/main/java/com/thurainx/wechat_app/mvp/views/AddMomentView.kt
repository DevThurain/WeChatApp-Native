package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.FileVO

interface AddMomentView : BasedView {
    fun navigateBack()
    fun pickFiles()
    fun onBindUserData(name: String, profileImage: String)
    fun showLoadingDialog()
    fun dismissLoadingDialog()
    fun onFileRemove(fileVO: FileVO)
}