package com.thurainx.wechat_app.network.cloud_firestore

import android.graphics.Bitmap
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MomentVO

interface CloudFireStoreApi {
    fun registerUser(
        name: String,
        phone: String,
        password: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun loginUser(
        phone: String,
        password: String,
        onSuccess: (name: String,phone: String, dob: String, gender: String, profileImage: String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun uploadProfilePicture(
        phone: String,
        bitmap: Bitmap?, onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun uploadMoment(
        text: String,
        fileList: List<FileVO>,
        phone: String,
        name: String,
        profileImage: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMoments(
        onSuccess: (List<MomentVO>) -> Unit,
        onFailure: (String) -> Unit
    )

}