package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.ContentVO
import com.thurainx.wechat_app.delegate.ContentDelegate
import com.thurainx.wechat_app.views.view_holders.ContentViewHolder

class ContentAdapter(val contentDelegate: ContentDelegate) : RecyclerView.Adapter<ContentViewHolder>() {
    var mDataList = listOf<ContentVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_content, parent,false)
        return ContentViewHolder(view,contentDelegate)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ContentVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}