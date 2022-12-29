package com.thurainx.wechat_app.data.models

import android.graphics.Bitmap
import com.thurainx.wechat_app.data.vos.*
import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApi
import com.thurainx.wechat_app.network.cloud_firestore.CloudFireStoreApiImpl
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApi
import com.thurainx.wechat_app.network.realtime_database.RealTimeDatabaseApiImpl

object WeChatModelImpl : WeChatModel {
    override var mCloudFireStoreApi: CloudFireStoreApi = CloudFireStoreApiImpl
    override var mRealTimeDatabaseApi: RealTimeDatabaseApi = RealTimeDatabaseApiImpl


    override fun registerUser(
        id: String,
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.registerUser(
            id,
            name,
            phone,
            password,
            dob,
            gender,
            onSuccess,
            onFailure
        )
    }

    override fun loginUser(
        id: String,
        password: String,
        onSuccess: (name: String, phone: String, dob: String, gender: String, profileImage: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.loginUser(id, password, onSuccess, onFailure)
    }

    override fun updateProfile(
        id: String,
        bitmap: Bitmap?,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.uploadProfilePicture(id, bitmap, onSuccess, onFailure)
    }

    override fun uploadMoment(
        text: String,
        fileList: List<FileVO>,
        id: String,
        name: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.uploadMoment(
            text,
            fileList,
            id,
            name,
            profileImage,
            onSuccess,
            onFailure
        )
    }

    override fun getMoments(
        id: String,
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.getMoments(id, onSuccess, onFailure)
    }

    override fun likeMoment(
        like: Boolean,
        momentMillis: String,
        id: String,
        totalLike: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.likeMoment(like, momentMillis, id, totalLike, onSuccess, onFailure)
    }

    override fun addContacts(
        selfId: String,
        friendId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.addContacts(selfId, friendId, onSuccess, onFailure)
    }

    override fun getContacts(
        id: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mCloudFireStoreApi.getContacts(id, onSuccess, onFailure)
    }

    override fun addMessage(
        contactVO: ContactVO,
        messageVO: MessageVO, fileList: List<FileVO>, onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.addMessage(contactVO, messageVO, fileList, onSuccess, onFailure)
    }

    override fun getMessagesForChatRoom(
        ownId: String,
        otherId: String,
        onSuccess: (List<MessageVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getMessagesForChatRoom(ownId, otherId, onSuccess, onFail)
    }

    override fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getLastMessage(ownId,onSuccess, onFail)
    }

    override fun createGroup(
        name: String,
        bitmap: Bitmap,
        contactList: List<ContactVO>,
        onSuccess: () -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.createGroup(name, bitmap, contactList, onSuccess, onFail)
    }

    override fun getGroups(
        selfId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFail: (String) -> Unit
    ) {
        mRealTimeDatabaseApi.getGroups(selfId, onSuccess, onFail)
    }

}