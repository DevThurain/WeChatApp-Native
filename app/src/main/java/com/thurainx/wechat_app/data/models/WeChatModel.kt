package com.thurainx.wechat_app.data.models

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.thurainx.wechat_app.data.vos.*
import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApi
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApi

interface WeChatModel {
    var mCloudFireStoreApi: CloudFireStoreApi
    var mRealTimeDatabaseApi: RealTimeDatabaseApi

    fun registerUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun loginUser(
        id: String,
        password: String,
        onSuccess: (name: String, phone: String, dob: String, gender: String, profileImage: String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun updateProfile(
        id: String,
        bitmap: Bitmap?,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun uploadMoment(
        text: String,
        fileList: List<FileVO>,
        id: String,
        name: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun likeMoment(
        like: Boolean,
        momentMillis: String,
        id: String,
        totalLike: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun bookMarkMoment(
        isBookMark: Boolean,
        momentMillis: String,
        id: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun addContacts(
        selfId: String,
        friendId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getContacts(
        id: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFailure: (String) -> Unit
    )

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

    fun getGroupLastMessage(
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

    fun getGroupMessages(
        groupId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    )

    fun addMessageToGroup(
        groupId: String,
        messageVO: MessageVO,
        fileList: List<FileVO>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getBookMarkMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun updateUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun removeLatestMessageListener(ownId: String)

    fun removeGroupListListener(selfId: String)

    fun removeChatRoomListener(ownId: String,otherId: String)

    fun removeGroupChatRoomListener(groupId: String)




}