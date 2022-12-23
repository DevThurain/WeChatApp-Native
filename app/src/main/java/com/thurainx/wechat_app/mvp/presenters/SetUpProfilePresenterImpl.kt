package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.SetUpProfileView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore

class SetUpProfilePresenterImpl : SetUpProfilePresenter, AbstractBasedPresenter<SetUpProfileView>() {

    var mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null

    override fun onTapPicture() {
        mView.pickImageFromGallery()
    }

    override fun uploadPicture(phone: String, bitmap: Bitmap?) {
        mWeChatModel.updateProfile(phone,bitmap, onSuccess = {
            dataStore?.writeToRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE,it)
            Log.d("navigate","navigate to main screen")
            mView.navigateToNavigationScreen()
        }, onFailure = {
            mView.showErrorMessage(it)
        })
    }


    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore
    }

}