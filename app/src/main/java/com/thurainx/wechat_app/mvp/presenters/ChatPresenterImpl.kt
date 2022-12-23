package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.ChatView

class ChatPresenterImpl: ChatPresenter, AbstractBasedPresenter<ChatView>() {

    val mWeChatModel = WeChatModelImpl

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }

    override fun onTapChat() {
        mView.navigateToChatRoomScreen()
    }
}