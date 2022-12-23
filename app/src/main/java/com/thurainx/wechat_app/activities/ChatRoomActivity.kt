package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.adapters.ContentAdapter
import com.thurainx.wechat_app.data.vos.ContentVO
import com.thurainx.wechat_app.mvp.presenters.ChatRoomPresenter
import com.thurainx.wechat_app.mvp.presenters.ChatRoomPresenterImpl
import com.thurainx.wechat_app.mvp.views.ChatRoomView
import com.thurainx.wechat_app.utils.CONTENT
import com.thurainx.wechat_app.utils.DEFAULT_CONTENT
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity(), ChatRoomView {
    lateinit var mPresenter: ChatRoomPresenter
    lateinit var mContentAdapter: ContentAdapter

    companion object {
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