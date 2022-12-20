package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.OtpView

class OtpPresenterImpl : OtpPresenter, AbstractBasedPresenter<OtpView>() {

    var mWeChatModel = WeChatModelImpl

    override fun onTapVerify(
        name: String,
        password: String,
        phone: String,
        dob: String,
        gender: String
    ) {
        mWeChatModel.registerUser(
            name = name,
            phone = phone,
            password = password,
            dob = dob,
            gender = gender,
            onSuccess = {
                mView.navigateToSetUpProfileScreen()
            },
            onFailure = { message ->
                mView.showErrorMessage(message)
            })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }
}