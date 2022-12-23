package com.thurainx.wechat_app.mvp.presenters

import com.thurainx.wechat_app.delegate.ChatDelegate
import com.thurainx.wechat_app.mvp.views.ChatView

interface ChatPresenter : BasedPresenter<ChatView>, ChatDelegate {

}