package com.thurainx.wechat_app.delegate

import com.thurainx.wechat_app.data.vos.FileVO

interface FileDelegate {
    fun onTapDelete(fileVO: FileVO)
}