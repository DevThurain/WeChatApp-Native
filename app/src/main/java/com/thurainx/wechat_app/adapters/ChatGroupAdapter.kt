package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.ChatGroupVO
import com.thurainx.wechat_app.views.view_holders.ChatGroupViewHolder

class ChatGroupAdapter() : RecyclerView.Adapter<ChatGroupViewHolder>() {
    var mDataList = listOf<ChatGroupVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_chat_group, parent,false)
        return ChatGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatGroupViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ChatGroupVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}