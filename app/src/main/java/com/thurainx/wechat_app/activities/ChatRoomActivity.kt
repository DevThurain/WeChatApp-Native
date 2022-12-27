package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.adapters.ContentAdapter
import com.thurainx.wechat_app.adapters.MessageAdapter
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.ContentVO
import com.thurainx.wechat_app.mvp.presenters.ChatRoomPresenter
import com.thurainx.wechat_app.mvp.presenters.ChatRoomPresenterImpl
import com.thurainx.wechat_app.mvp.views.ChatRoomView
import com.thurainx.wechat_app.utils.CONTENT
import com.thurainx.wechat_app.utils.DEFAULT_CONTENT
import com.thurainx.wechat_app.utils.TEMP_MESSAGE_LIST
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity(), ChatRoomView {
    lateinit var mPresenter: ChatRoomPresenter
    lateinit var mContentAdapter: ContentAdapter
    lateinit var mMessageAdapter: MessageAdapter

    companion object {
        var mContact : ContactVO? = null
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, ChatRoomActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setUpPresenter()
        setUpRecyclerView()
        bindData()

        mPresenter.onUiReady(this, this)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[ChatRoomPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView() {
        mContentAdapter = ContentAdapter(mPresenter)
        rvContent.adapter = mContentAdapter
        mContentAdapter.setNewData(DEFAULT_CONTENT)

        mMessageAdapter = MessageAdapter()
        mMessageAdapter.setId("4LcCrDCK1xMniCZ3iq9M2oFKvhx2")
        rvMessage.adapter = mMessageAdapter
        mMessageAdapter.setNewData(TEMP_MESSAGE_LIST)

    }

    private fun bindData(){
        mContact?.let {
            Glide.with(this)
                .load(it.photoUrl)
                .into(ivChatRoomProfile)

            tvChatroomName.text = it.name
            tvChatroomActiveStatus.text = "online"
        }
    }

    override fun onTapContent(content: CONTENT) {
        val newList = DEFAULT_CONTENT.map {
            ContentVO(
                content = it.content,
                image = it.image,
                isSelected = (it.content == content)
            )
        }.toList()
        mContentAdapter.setNewData(newList)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }
}