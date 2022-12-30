package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.mvp.views.CreateGroupView
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.readFromRxDatastore
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CreateGroupPresenterImpl : AbstractBasedPresenter<CreateGroupView>(), CreateGroupPresenter {

    val mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null
    var mId: String = ""
    var mName: String = ""
    var mPhotoUrl: String = ""
    private var selectedContactList: ArrayList<ContactVO> = arrayListOf()
    override fun pickGroupImage() {
        mView.pickGroupImage()
    }

    override fun onTapBack() {
        mView.navigateBack()
    }

    override fun createGroup(name: String, bitmap: Bitmap) {
        val selfContactVO = ContactVO(
            id = mId,
            name = mName,
            photoUrl = mPhotoUrl
        )

        selectedContactList.add(selfContactVO)
        mWeChatModel.createGroup(
            name = name,
            bitmap = bitmap,
            contactList = selectedContactList,
            onSuccess = {
                mView.createGroupSuccess()
            },
            onFail = {
                mView.showErrorMessage(it)
            }
        )
    }


    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            getContacts(mId)
        }

        getUserData()
    }

    override fun onTapContact(contactVO: ContactVO) {

    }

    override fun onSelectContact(isSelect: Boolean, contactVO: ContactVO) {
        if (isSelect) {
            selectedContactList.add(contactVO)
        } else {
            selectedContactList.remove(contactVO)
        }
//        Log.d("contacts",selectedContactList.toString())
    }

    private fun getContacts(id: String) {
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

    private fun getUserData() {
        Single.zip(
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_NAME) ?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PHONE)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_DOB) ?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_GENDER)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE) ?.first("-") ?: Single.just("-"),
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