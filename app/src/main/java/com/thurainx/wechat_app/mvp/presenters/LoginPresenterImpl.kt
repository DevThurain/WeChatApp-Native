package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.preferences.rxjava3.rxPreferencesDataStore
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.clearRxDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.readFromRxDatastore
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginPresenterImpl : LoginPresenter, AbstractBasedPresenter<LoginView>() {

    var mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null



    override fun onTapLogin(phone: String, password: String) {
        mWeChatModel.loginUser(phone, password, onSuccess = { name, _, dob, gender, profileImage ->
            saveUserToDatastore(name, phone, dob, gender, profileImage)
            mView.navigateToNavigationScreen()
        }, onFailure = {
            mView.showErrorMessage(it)
        })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

    }

    private fun saveUserToDatastore(name: String, phone: String,dob: String, gender: String, profileImage: String){
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_NAME, name)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PHONE, phone)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_DOB, dob)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_GENDER, gender)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE, profileImage)
    }
}