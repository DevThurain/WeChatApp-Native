package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.views.view_holders.ActiveChatViewHolder

class ActiveChatAdapter() : RecyclerView.Adapter<ActiveChatViewHolder>() {
    var mDataList = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_active_chat, parent,false)
        return ActiveChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActiveChatViewHolder, position: Int) {
//        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return 5
    }

    fun setNewData(dataList: List<String>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}