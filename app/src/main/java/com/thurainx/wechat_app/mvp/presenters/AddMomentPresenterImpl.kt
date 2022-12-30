package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.mvp.views.AddMomentView
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.MomentView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.clearRxDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.readFromRxDatastore
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class AddMomentPresenterImpl : AddMomentPresenter, AbstractBasedPresenter<AddMomentView>() {

    var mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null
    var mId: String = ""
    var mName: String = ""
    var mProfileImage: String = ""


    override fun onTapBack() {
        mView.navigateBack()
    }


    override fun onTapPickFile() {
        mView.pickFiles()
    }

    override fun uploadMoment(text: String, fileList: List<FileVO>) {
        mView.showLoadingDialog()
        mWeChatModel.uploadMoment(text, fileList, mId, mName, mProfileImage, onSuccess = {
            mView.dismissLoadingDialog()
            Log.d("multi_file", "moment create success")
        }, onFailure = {
            mView.showErrorMessage(it)
            mView.dismissLoadingDialog()
        })
    }


    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        Single.zip(
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_ID)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_NAME) ?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE) ?.first("-") ?: Single.just("-"),
        ) { id, name, profile ->
            Log.d("rx_read", "$name - $profile")
            return@zip mapOf(
                FIRE_STORE_REF_ID to id,
                FIRE_STORE_REF_NAME to name,
                FIRE_STORE_REF_PROFILE_IMAGE to profile
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mId = it[FIRE_STORE_REF_ID].toString()
                mName = it[FIRE_STORE_REF_NAME].toString()
                mProfileImage = it[FIRE_STORE_REF_PROFILE_IMAGE].toString()
                mView.onBindUserData(mName, mProfileImage)
            }, {
                Log.d("rx", it.message.toString())
            })


    }

    override fun onTapDelete(fileVO: FileVO) {
        mView.onFileRemove(fileVO)
    }


}