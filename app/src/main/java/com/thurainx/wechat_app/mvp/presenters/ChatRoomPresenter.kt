package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.delegate.ContentDelegate
import com.thurainx.wechat_app.mvp.views.ChatRoomView

interface ChatRoomPresenter : BasedPresenter<ChatRoomView>, ContentDelegate {

}