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
import com.thurainx.wechat_app.data.models.AuthModelImpl
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
    var mAuthModel = AuthModelImpl
    var dataStore: RxDataStore<Preferences>? = null

    override fun onTapLogin(phone: String, password: String) {
        mAuthModel.loginUser(phone, password, onSuccess = { id ->
            loginToFireStore(id, password, onSuccess = { name, _, dob, gender, profileImage ->
                Log.d("id",id)
                saveUserToDatastore(id, name, phone, dob, gender, profileImage, password)
                mView.navigateToNavigationScreen()
            }, onFailure = { message ->
                mView.showErrorMessage(message)
            })
        }, onFailure = { message ->
            mView.showErrorMessage(message)
        })
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

    }

    private fun saveUserToDatastore(
        id: String,
        name: String,
        phone: String,
        dob: String,
        gender: String,
        profileImage: String,
        password: String
    ) {
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_ID, id)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_NAME, name)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PHONE, phone)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_DOB, dob)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_GENDER, gender)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE, profileImage)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PASSWORD, password)

    }

    private fun loginToFireStore(
        id: String,
        password: String,
        onSuccess: (name: String, phone: String, dob: String, gender: String, profileImage: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mWeChatModel.loginUser(id, password,
            onSuccess = { name, phone, dob, gender, profileImage ->
            onSuccess(name, phone, dob, gender, profileImage)
        }, onFailure = {
            onFailure(it)
        })
    }
}