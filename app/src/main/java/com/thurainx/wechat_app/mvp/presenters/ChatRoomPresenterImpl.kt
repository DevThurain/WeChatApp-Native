package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.data.vos.MessageVO
import com.thurainx.wechat_app.mvp.views.ChatRoomView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.readFromRxDatastore
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class ChatRoomPresenterImpl: ChatRoomPresenter, AbstractBasedPresenter<ChatRoomView>() {
    var dataStore: RxDataStore<Preferences>? = null
    val mWeChatModel = WeChatModelImpl
    var mId: String = ""
    var mName: String = ""
    var mPhotoUrl: String = ""

    override fun onUiReadyWithId(context: Context, owner: LifecycleOwner,otherId: String) {
        dataStore = context.userDataStore

        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            mWeChatModel.getMessagesForChatRoom(
                ownId = mId,
                otherId = otherId,
                onSuccess = { messages ->
                    mView.bindMessages(ownId = mId, messageList = messages)
                },
                onFail = { error ->
                    mView.showErrorMessage(error)
                }
            )
        }
        getUserData()
    }

    override fun sentMessage(otherId: String, fileList: List<FileVO>, message: MessageVO) {
        Log.d("firebase","call sent message")
        mWeChatModel.addMessage(
            otherId = otherId,
            messageVO = MessageVO(
                text = message.text,
                millis = message.millis,
                photoList = message.photoList,
                videoLink = message.videoLink,
                name = mName,
                id = mId,
                profileImage = mPhotoUrl
            )
        )
    }

    override fun onTapBack() {
        mView.navigateBack()
    }

    override fun onTapCamera() {
        mView.navigateToCameraScreen()
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {

    }

    override fun onTapContent(content: CONTENT) {
        mView.onTapContent(content)
    }

    override fun onTapDelete(fileVO: FileVO) {
        mView.onFileRemove(fileVO)
    }

    private fun getUserData(){
        Observable.zip(
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_NAME) ?: Observable.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PHONE) ?: Observable.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_DOB) ?: Observable.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_GENDER) ?: Observable.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE) ?: Observable.just("-")
        ) { name, phone, dob, gender, profile ->
            Log.d("rx_read", "$name - $profile")
            return@zip mapOf(
                FIRE_STORE_REF_NAME to name,
                FIRE_STORE_REF_PHONE to phone,
                FIRE_STORE_REF_DOB to dob,
                FIRE_STORE_REF_GENDER to gender,
                FIRE_STORE_REF_PROFILE_IMAGE to profile,
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mName = it[FIRE_STORE_REF_NAME].toString()
                mPhotoUrl = it[FIRE_STORE_REF_PROFILE_IMAGE].toString()
            }, {
                Log.d("rx", it.message.toString())
            })
    }


}