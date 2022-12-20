package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.LoginView

class LoginPresenterImpl : LoginPresenter, AbstractBasedPresenter<LoginView>() {

    var mWeChatModel = WeChatModelImpl

    override fun onTapLogin(phone: String, password: String) {
        mWeChatModel.loginUser(phone, password, onSuccess = {
            mView.navigateToNavigationScreen()
        }, onFailure = {
            mView.showErrorMessage(it)
        })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }
}