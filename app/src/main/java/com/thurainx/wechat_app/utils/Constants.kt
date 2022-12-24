package com.thurainx.wechat_app.utils

import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.ContentVO

const val DEFAULT_PROFILE_IMAGE = "https://firebasestorage.googleapis.com/v0/b/wechatapp---padc.appspot.com/o/profiles%2Fb9e5775c-04f4-4ef9-85ff-2c6671d626c6?alt=media&token=f9f0d161-7940-40e0-9be3-17f1c417e5fe"

const val FIRE_STORE_REF_ID = "id"
const val FIRE_STORE_REF_NAME = "name"
const val FIRE_STORE_REF_PHONE = "phone"
const val FIRE_STORE_REF_PASSWORD = "password"
const val FIRE_STORE_REF_DOB = "dob"
const val FIRE_STORE_REF_GENDER = "gender"
const val FIRE_STORE_REF_PROFILE_IMAGE = "profileImage"
const val FIRE_STORE_REF_TEXT = "text"
const val FIRE_STORE_REF_PHOTO_LIST = "photoList"
const val FIRE_STORE_REF_VIDEO_LINK = "videoLink"
const val FIRE_STORE_REF_MILLIS = "millis"
const val FIRE_STORE_REF_LIKE_COUNT = "likeCount"
const val FIRE_STORE_REF_LIKE = "like"

const val EXTRA_NAME = "EXTRA_NAME"
const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
const val EXTRA_DOB = "EXTRA_DOB"
const val EXTRA_GENDER = "EXTRA_GENDER"



enum class CONTENT{
    IMAGE, CAMERA, GIF, LOCATION, VOICE
}
val DEFAULT_CONTENT = listOf<ContentVO>(
    ContentVO(
        content =CONTENT.IMAGE,
        image = R.drawable.ic_picture,
        isSelected = false
    ),
    ContentVO(
        content = CONTENT.CAMERA,
        image = R.drawable.ic_camera,
        isSelected = false
    ),
    ContentVO(
        content = CONTENT.GIF,
        image = R.drawable.ic_gif,
        isSelected = false
    ),
    ContentVO(
        content =CONTENT.LOCATION,
        image = R.drawable.ic_location,
        isSelected = false
    ),
    ContentVO(
        content = CONTENT.VOICE,
        image = R.drawable.ic_voice,
        isSelected = false
    )
)
