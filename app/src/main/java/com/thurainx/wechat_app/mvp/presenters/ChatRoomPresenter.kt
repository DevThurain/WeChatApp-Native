package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MessageVO
import com.thurainx.wechat_app.delegate.ContentDelegate
import com.thurainx.wechat_app.delegate.FileDelegate
import com.thurainx.wechat_app.mvp.views.ChatRoomView

interface ChatRoomPresenter : BasedPresenter<ChatRoomView>, ContentDelegate, FileDelegate {
    fun onUiReadyWithId(context: Context, owner: LifecycleOwner, otherId: String)
    fun sentMessage(contactVO: ContactVO, fileList: List<FileVO>, message: MessageVO)

    fun onTapBack()
    fun onTapCamera()
}