package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MessageVO
import com.thurainx.wechat_app.utils.CONTENT

interface ChatRoomView : BasedView {
    fun onTapContent(content: CONTENT)
    fun bindMessages(ownId: String,messageList: List<MessageVO>)
    fun navigateBack()
    fun navigateToCameraScreen()
    fun onFileRemove(fileVO: FileVO)

}