package com.thurainx.wechat_app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.activities.ChatRoomActivity
import com.thurainx.wechat_app.adapters.ActiveChatAdapter
import com.thurainx.wechat_app.adapters.ActiveUserAdapter
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.mvp.presenters.ChatPresenter
import com.thurainx.wechat_app.mvp.presenters.ChatPresenterImpl
import com.thurainx.wechat_app.mvp.views.ChatView
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChatFragment : Fragment(), ChatView {

    lateinit var mActiveUserAdapter : ActiveUserAdapter
    lateinit var mActiveChatAdapter: ActiveChatAdapter
    lateinit var mPresenter: ChatPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setUpRecyclerView(view)

        mPresenter.onUiReady(requireContext(), requireActivity())
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[ChatPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView(view: View?){
        mActiveUserAdapter = ActiveUserAdapter(mPresenter)
        view?.rvActiveUser?.adapter = mActiveUserAdapter

        mActiveChatAdapter = ActiveChatAdapter(mPresenter)
        view?.rvActiveChat?.adapter = mActiveChatAdapter
    }

    override fun bindContacts(contactList: List<ContactVO>) {
        Log.d("contactList",contactList.toString())
        mActiveUserAdapter.setNewData(contactList)
    }

    override fun bindLastMessage(contactList: List<ContactVO>) {
        Log.d("latestMessageList",contactList.toString())
        mActiveChatAdapter.setNewData(contactList)
    }

    override fun navigateToChatRoomScreen(contactVO: ContactVO) {
        val intent = ChatRoomActivity.getIntent(requireContext())
        ChatRoomActivity.mContact = contactVO
        ChatRoomActivity.isGroup = contactVO.isGroup
        startActivity(intent)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

}