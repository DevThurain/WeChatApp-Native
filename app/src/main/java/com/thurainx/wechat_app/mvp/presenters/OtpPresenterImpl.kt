package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.AuthModelImpl
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.OtpView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore

class OtpPresenterImpl : OtpPresenter, AbstractBasedPresenter<OtpView>() {

    var mWeChatModel = WeChatModelImpl
    var mAuthModel = AuthModelImpl
    var dataStore: RxDataStore<Preferences>? = null


    override fun onTapVerify(
        name: String,
        password: String,
        phone: String,
        dob: String,
        gender: String
    ) {
        mAuthModel.registerUser(
            phone = phone,
            password = password,
            onSuccess = { id ->
                registerToFireStore(
                    id = id,
                    name = name,
                    phone = phone,
                    password = password,
                    dob = dob,
                    gender = gender,
                    onSuccess = {
                        Log.d("id",id)
                        saveUserToDatastore(id, name, phone, dob, gender, password)
                        mView.navigateToSetUpProfileScreen()
                    },
                    onFailure = { message ->
                        mView.showErrorMessage(message)
                    }
                )
            },
            onFailure = { message ->
                mView.showErrorMessage(message)
            }
        )


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
        password: String
    ) {
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_ID, id)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_NAME, name)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PHONE, phone)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_DOB, dob)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_GENDER, gender)
        dataStore?.writeToRxDatastore(FIRE_STORE_REF_PASSWORD, password)
    }

    private fun registerToFireStore(
        id: String,
        name: String,
        password: String,
        phone: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        mWeChatModel.registerUser(
            id = id,
            name = name,
            phone = phone,
            password = password,
            dob = dob,
            gender = gender,
            onSuccess = {
                onSuccess()
            },
            onFailure = { message ->
                onFailure(message)
            })
    }
}