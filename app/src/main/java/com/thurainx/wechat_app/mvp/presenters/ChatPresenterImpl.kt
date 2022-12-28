package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.mvp.views.ChatView
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.FIRE_STORE_REF_ID

class ChatPresenterImpl: ChatPresenter, AbstractBasedPresenter<ChatView>() {

    val mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null
    var mId: String = ""

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            getContacts(mId)
            getLatestMessage(mId)
        }

    }

    override fun onTapChat(contactVO: ContactVO) {
        mView.navigateToChatRoomScreen(contactVO)
    }


    private fun getContacts(id: String){
        mWeChatModel.getContacts(
            id = id,
            onSuccess = {
                mView.bindContacts(it)
            },
            onFailure = {
                mView.showErrorMessage(it)
            }
        )
    }

    private fun getLatestMessage(id: String){
        mWeChatModel.getLastMessage(
            ownId = id,
            onSuccess = {
                        mView.bindLastMessage(it)
            },
            onFail = {}
        )
    }

}