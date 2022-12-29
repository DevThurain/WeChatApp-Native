package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.GroupVO
import com.thurainx.wechat_app.delegate.GroupDelegate
import com.thurainx.wechat_app.views.view_holders.ChatGroupViewHolder

class ChatGroupAdapter(val groupDelegate: GroupDelegate) : RecyclerView.Adapter<ChatGroupViewHolder>() {
    var mDataList = listOf<GroupVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_chat_group, parent,false)
        return ChatGroupViewHolder(view, groupDelegate)
    }

    override fun onBindViewHolder(holder: ChatGroupViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<GroupVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}