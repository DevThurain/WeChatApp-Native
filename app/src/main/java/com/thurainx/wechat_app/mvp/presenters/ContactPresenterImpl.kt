package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.GroupVO
import com.thurainx.wechat_app.mvp.views.ContactView
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.FIRE_STORE_REF_ID
import com.thurainx.wechat_app.utils.FIRE_STORE_REF_PHONE

class ContactPresenterImpl : AbstractBasedPresenter<ContactView>(), ContactPresenter {

    val mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null
    var mId: String = ""

    override fun onTapAddContact() {
        mView.navigateToQrScannerScreen()
    }

    override fun onTapCreateGroup() {
        mView.navigateToCreateGroupScreen()
    }

    override fun refreshGroupList() {
        getGroups(mId)
    }


    override fun addContact(friendId: String) {
        mWeChatModel.addContacts(
            selfId = mId,
            friendId = friendId,
            onSuccess = {
                getContacts(mId)
            },
            onFailure = {
                mView.showErrorMessage(it)
            }
        )
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            getContacts(mId)
            getGroups(mId)
        }
    }

    override fun onTapContact(contactVO: ContactVO) {
        mView.navigateToChatRoomScreen(contactVO)
    }

    override fun onSelectContact(isSelect: Boolean, contactVO: ContactVO) {

    }

    override fun onTapGroup(groupVO: GroupVO) {
        mView.navigateToGroupChatScreen(groupVO)
    }

    private fun getContacts(id: String){
        mWeChatModel.getContacts(
            id = mId,
            onSuccess = {
                mView.bindContacts(it)
            },
            onFailure = {
                mView.showErrorMessage(it)
            }
        )
    }
    private fun getGroups(id: String){
        mWeChatModel.getGroups(
            selfId = mId,
            onSuccess = {
                mView.bindGroups(it)
            },
            onFail = {
                mView.showErrorMessage(it)
            }
        )
    }

}