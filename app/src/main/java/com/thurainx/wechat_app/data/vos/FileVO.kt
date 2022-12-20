package com.thurainx.wechat_app.data.vos

import android.graphics.Bitmap

data class FileVO(
    val bitmap: Bitmap,
    val isMovie: Boolean = false
)
