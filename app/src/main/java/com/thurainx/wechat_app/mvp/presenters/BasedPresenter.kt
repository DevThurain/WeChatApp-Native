package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.mvp.views.BasedView

interface BasedPresenter<V : BasedView>{
    fun initView(view: V)
    fun onUiReady(context: Context, owner: LifecycleOwner)
}