package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.utils.CONTENT

interface ChatRoomView : BasedView {
    fun onTapContent(content: CONTENT)
}