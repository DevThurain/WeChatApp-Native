package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.ChatRoomView
import com.thurainx.wechat_app.mvp.views.ChatView
import com.thurainx.wechat_app.utils.CONTENT

class ChatRoomPresenterImpl: ChatRoomPresenter, AbstractBasedPresenter<ChatRoomView>() {

    val mWeChatModel = WeChatModelImpl

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }

    override fun onTapContent(content: CONTENT) {
        mView.onTapContent(content)
    }


}