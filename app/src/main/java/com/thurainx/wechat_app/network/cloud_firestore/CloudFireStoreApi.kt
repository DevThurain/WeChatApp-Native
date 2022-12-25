package com.thurainx.wechat_app.network.cloud_firestore

import android.graphics.Bitmap
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MomentVO

interface CloudFireStoreApi {
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

    fun uploadProfilePicture(
        id: String,
        bitmap: Bitmap?, onSuccess: (String) -> Unit,
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

}