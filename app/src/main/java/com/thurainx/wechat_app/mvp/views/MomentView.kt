package com.thurainx.wechat_app.mvp.views

import com.thurainx.wechat_app.data.vos.MomentVO

interface MomentView : BasedView {
    fun navigateToAddMomentScreen()
    fun bindMoments(momentList: List<MomentVO>)
}