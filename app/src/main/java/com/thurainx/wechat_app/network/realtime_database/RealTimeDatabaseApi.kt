package com.thurainx.wechat_app.network.realtime_database

import com.google.android.gms.tasks.OnFailureListener
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MessageVO

interface RealTimeDatabaseApi {

    fun addMessage(
        otherId: String,
        messageVO: MessageVO,
//        fileVO: FileVO
    )

    fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    )
}