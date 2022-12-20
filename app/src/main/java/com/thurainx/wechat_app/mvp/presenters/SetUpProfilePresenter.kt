package com.thurainx.wechat_app.mvp.presenters

import android.graphics.Bitmap
import com.thurainx.wechat_app.mvp.views.SetUpProfileView

interface SetUpProfilePresenter : BasedPresenter<SetUpProfileView> {
    fun onTapPicture()
    fun uploadPicture(phone: String,bitmap: Bitmap?)
}