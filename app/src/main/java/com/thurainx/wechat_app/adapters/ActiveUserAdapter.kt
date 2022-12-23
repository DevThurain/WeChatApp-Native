package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.views.view_holders.ActiveUserViewHolder

class ActiveUserAdapter() : RecyclerView.Adapter<ActiveUserViewHolder>() {
    var mDataList = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_active_user, parent,false)
        return ActiveUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActiveUserViewHolder, position: Int) {
//        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return 8
    }

    fun setNewData(dataList: List<String>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}