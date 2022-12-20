package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.SetUpProfileView

class SetUpProfilePresenterImpl : SetUpProfilePresenter, AbstractBasedPresenter<SetUpProfileView>() {

    var mWeChatModel = WeChatModelImpl

    override fun onTapPicture() {
        mView.pickImageFromGallery()
    }

    override fun uploadPicture(phone: String, bitmap: Bitmap?) {
        mWeChatModel.updateProfile(phone,bitmap, onSuccess = {
            mView.navigateToNavigationScreen()
        }, onFailure = {
            mView.showErrorMessage(it)
        })
    }


    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }
}