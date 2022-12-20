package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.OtpView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore

class OtpPresenterImpl : OtpPresenter, AbstractBasedPresenter<OtpView>() {

    var mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null


    override fun onTapVerify(
        name: String,
        password: String,
        phone: String,
        dob: String,
        gender: String
    ) {
        mWeChatModel.registerUser(
            name = name,
            phone = phone,
            password = password,
            dob = dob,
            gender = gender,
            onSuccess = {
                saveUserToDatastore(name,phone,dob,gender)
                mView.navigateToSetUpProfileScreen()
            },
            onFailure = { message ->
                mView.showErrorMessage(message)
            })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore


    }


    private fun saveUserToDatastore(name: String, phone: String,dob: String, gender: String){
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_NAME, name)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PHONE, phone)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_DOB, dob)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_GENDER, gender)
    }
}