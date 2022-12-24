package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.mvp.views.MomentView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.clearRxDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.readFromRxDatastore
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MomentPresenterImpl : MomentPresenter, AbstractBasedPresenter<MomentView>() {

    var mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null

    var mId: String = ""


    override fun onTapAddMoment() {
        mView.navigateToAddMomentScreen()
    }

    override fun onRefreshMoment() {
        mWeChatModel.getMoments(
            id = mId,
            onSuccess = {
                mView.bindMoments(it)
            },
            onFailure = {
                mView.showErrorMessage(it)
            }
        )
    }


    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        dataStore?.readQuick(FIRE_STORE_REF_NAME){
            Log.d("rx_read", it)
        }
        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            mWeChatModel.getMoments(
                id = mId,
                onSuccess = {
                    mView.bindMoments(it)
                },
                onFailure = { error ->
                    mView.showErrorMessage(error)
                }
            )
        }
        dataStore?.readQuick(FIRE_STORE_REF_DOB){
            Log.d("rx_read", it)
        }
        dataStore?.readQuick(FIRE_STORE_REF_GENDER){
            Log.d("rx_read", it)
        }
        dataStore?.readQuick(FIRE_STORE_REF_PROFILE_IMAGE){
            Log.d("rx_read", it)
        }





    }

    override fun onTapLike(momentMillis: String, totalLike: Int,isLike: Boolean,onSuccess: () -> Unit) {
        mWeChatModel.likeMoment(
            like = isLike,
            id = mId,
            totalLike = totalLike,
            momentMillis = momentMillis,
            onSuccess = {
                Log.d("reaction", "reaction success")
                onSuccess()
            },
            onFailure = {
                mView.showErrorMessage(it)
            },
        )

    }

    override fun onTapBookmark(momentMillis: String, isBookmark: Boolean) {

    }


}