package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.mvp.views.SettingView
import com.thurainx.wechat_app.network.auth.AuthManagerImpl
import com.thurainx.wechat_app.utils.DataStoreUtils.clearRxDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore

class SettingPresenterImpl: AbstractBasedPresenter<SettingView>(), SettingPresenter {

    val mAuthManager = AuthManagerImpl
    var dataStore: RxDataStore<Preferences>? = null

    override fun onTapLogout() {
        mAuthManager.logoutUser()
        dataStore?.clearRxDataStore()
        mView.navigateToGreetingScreen()
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

    }
}