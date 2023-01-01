package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.mvp.views.ChatView
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.FIRE_STORE_REF_ID

class ChatPresenterImpl : ChatPresenter, AbstractBasedPresenter<ChatView>() {

    val mWeChatModel = WeChatModelImpl
    var dataStore: RxDataStore<Preferences>? = null
    var mId: String = ""
    var normalContactList: ArrayList<ContactVO> = arrayListOf()
    var groupContactList: ArrayList<ContactVO> = arrayListOf()
    lateinit var latestMessageListener: ValueEventListener
    private val database: DatabaseReference = Firebase.database.reference



    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            getContacts(mId)
            getLatestMessage(mId, owner)
        }

    }

    override fun onTapChat(contactVO: ContactVO) {
        mView.navigateToChatRoomScreen(contactVO)
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

    private fun getLatestMessage(id: String, owner: LifecycleOwner) {
        mWeChatModel.getLastMessage(
            ownId = id,
            onSuccess = {
                normalContactList.clear()
                normalContactList = ArrayList(it)
                mView.bindLastMessage(normalContactList.plus(groupContactList))
            },
            onFail = {
                mView.showErrorMessage(it)
            }
        )

        mWeChatModel.getGroupLastMessage(
            ownId = id,
            onSuccess = {
                groupContactList.clear()
                groupContactList = ArrayList(it)
                mView.bindLastMessage(groupContactList.plus(normalContactList))
            },
            onFail = {
                mView.showErrorMessage(it)
            }
        )

    }

    private fun getLastMessage(
        ownId: String,
        onSuccess: (List<ContactVO>) -> Unit,
        onFail: (String) -> Unit
    ){

        latestMessageListener = database.child("contactsAndMessages")
            .child(ownId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    onFail("Cannot get data")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val contactList = arrayListOf<ContactVO>()
                    snapshot.children.forEach { dataSnapShot ->
                        val map = dataSnapShot.value as Map<String, *>
                        Log.d("snap_shot_list", (map["contact"].toString()))

                        if (map["contact"].toString() != "null") {
                            val contactMap = map["contact"] as Map<String, *>
                            val messageMap = map["messages"] as Map<String, *>
                            val lastKey =
                                messageMap.toSortedMap(compareBy<String> { it.length }.thenBy { it })
                                    .lastKey()
                            val latestMessageMap = messageMap[lastKey] as Map<String, *>
                            var latestMessage = ""

                            if (latestMessageMap["text"].toString().isNotEmpty()) {
                                latestMessage = latestMessageMap["text"].toString()
                            } else if (latestMessageMap["photoList"].toString() != "null") {
                                latestMessage = "sent a photo."
                            } else if (latestMessageMap["videoLink"].toString().isNotEmpty()) {
                                latestMessage = "sent a video"
                            }
                            Log.d("snap_shot_list", latestMessage)

                            val contact = ContactVO(
                                id = contactMap["id"].toString(),
                                name = contactMap["name"].toString(),
                                photoUrl = contactMap["photoUrl"].toString(),
                                lastMessage = latestMessage
                            )

                            contactList.add(contact)
                        }

                    }
                    onSuccess(contactList)
//                    contactLiveData.postValue(contactList)

                }
            })


    }

    override fun onCleared() {
        super.onCleared()
        mWeChatModel.removeLatestMessageListener(mId)
//        database.child("contactsAndMessages")
//            .child(mId).removeEventListener(latestMessageListener)
        Log.d("viewModel","clear")
    }

}