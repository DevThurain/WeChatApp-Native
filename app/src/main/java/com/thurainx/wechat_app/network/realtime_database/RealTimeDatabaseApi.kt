package com.thurainx.wechat_app.network.realtime_database

import android.graphics.Bitmap
import com.google.android.gms.tasks.OnFailureListener
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.GroupVO
import com.thurainx.wechat_app.data.vos.MessageVO

interface RealTimeDatabaseApi {

    fun addMessage(
        contactVO: ContactVO,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun createGroup(
        name: String,
        bitmap: Bitmap,
        contactList: List<ContactVO>,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    )

    fun getGroups(
        selfId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFail: (String) -> Unit
    )
}