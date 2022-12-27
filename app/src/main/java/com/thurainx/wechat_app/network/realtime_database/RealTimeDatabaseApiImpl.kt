package com.thurainx.wechat_app.network.realtime_database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.thurainx.wechat_app.data.vos.MessageVO

object RealTimeDatabaseApiImpl : RealTimeDatabaseApi {
    private val database: DatabaseReference = Firebase.database.reference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference


    override fun addMessage(
        ownId: String,
        messageVO: MessageVO,
    ) {
        database.child("contactsAndMessages").child(ownId)
            .child(messageVO.id)
            .setValue(messageVO)

        database.child("contactsAndMessages").child(messageVO.id)
            .child(ownId)
            .setValue(messageVO)
    }

    override fun getMessagesForChatRoom(ownId: String, otherId: String, onSuccess: (List<MessageVO>) -> Unit, onFail: (String) -> Unit) {
        database.child("contactsAndMessages")
            .child(ownId)
            .child(otherId)
            .addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                onFail(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = arrayListOf<MessageVO>()
                snapshot.children.forEach { dataSnapShot ->
                    dataSnapShot.getValue(MessageVO::class.java)?.let {
                        messageList.add(it)
                    }
                }
                onSuccess(messageList)
            }
        })
    }


}