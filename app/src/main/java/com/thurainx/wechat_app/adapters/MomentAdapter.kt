package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.MomentVO
import com.thurainx.wechat_app.views.view_holders.MomentViewHolder

class MomentAdapter() : RecyclerView.Adapter<MomentViewHolder>() {
    var mDataList = listOf<MomentVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_moment, parent,false)
        return MomentViewHolder(view)
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<MomentVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}