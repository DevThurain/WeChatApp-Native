package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.views.view_holders.SmartImageViewHolder

class SmartImageAdapter() : RecyclerView.Adapter<SmartImageViewHolder>() {
    private var mDataList = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_smart_image, parent,false)
        return SmartImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmartImageViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<String>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}