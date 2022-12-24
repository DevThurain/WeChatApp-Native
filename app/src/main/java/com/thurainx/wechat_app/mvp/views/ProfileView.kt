package com.thurainx.wechat_app.mvp.views

interface ProfileView : BasedView {
    fun bindProfileData(name: String, phone: String, dob: String, gender: String, profile: String)
    fun showEditDialog(name: String, phone: String, dob: String, gender: String)
    fun showQrDialog(qrCode: String)
}