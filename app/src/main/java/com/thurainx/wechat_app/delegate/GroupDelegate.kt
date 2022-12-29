package com.thurainx.wechat_app.delegate

import com.thurainx.wechat_app.data.vos.GroupVO

interface GroupDelegate {
    fun onTapGroup(groupVO: GroupVO)
}