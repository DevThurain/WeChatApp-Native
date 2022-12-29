package com.thurainx.wechat_app.delegate

interface MomentDelegate {
    fun onTapLike(momentMillis: String,totalLike: Int,isLike: Boolean,onSuccess : () -> Unit)
    fun onTapBookmark(momentMillis: String, isBookmark: Boolean, onSuccess: () -> Unit)
}