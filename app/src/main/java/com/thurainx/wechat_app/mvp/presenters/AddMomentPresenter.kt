package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.delegate.FileDelegate
import com.thurainx.wechat_app.delegate.MomentDelegate
import com.thurainx.wechat_app.mvp.views.AddMomentView
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.MomentView

interface AddMomentPresenter : BasedPresenter<AddMomentView>, FileDelegate {
    fun onTapBack()
    fun onTapPickFile()
    fun uploadMoment(text: String,fileList: List<FileVO>)

}