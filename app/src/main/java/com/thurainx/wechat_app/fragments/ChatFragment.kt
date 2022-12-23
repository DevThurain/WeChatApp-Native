package com.thurainx.wechat_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.adapters.ActiveChatAdapter
import com.thurainx.wechat_app.adapters.ActiveUserAdapter
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChatFragment : Fragment() {

    lateinit var mActiveUserAdapter : ActiveUserAdapter
    lateinit var mActiveChatAdapter: ActiveChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View?){
        mActiveUserAdapter = ActiveUserAdapter()
        view?.rvActiveUser?.adapter = mActiveUserAdapter

        mActiveChatAdapter = ActiveChatAdapter()
        view?.rvActiveChat?.adapter = mActiveChatAdapter
    }


}