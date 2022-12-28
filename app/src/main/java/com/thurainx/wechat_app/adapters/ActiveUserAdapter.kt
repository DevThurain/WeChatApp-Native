package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.delegate.ChatDelegate
import com.thurainx.wechat_app.views.view_holders.ActiveUserViewHolder

class ActiveUserAdapter(private val chatDelegate: ChatDelegate) : RecyclerView.Adapter<ActiveUserViewHolder>() {
    private var mDataList = listOf<ContactVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_active_user, parent,false)
        return ActiveUserViewHolder(view, chatDelegate)
    }

    override fun onBindViewHolder(holder: ActiveUserViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ContactVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}