package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.views.view_holders.FileViewHolder

class FileAdapter() : RecyclerView.Adapter<FileViewHolder>() {
    var mDataList = listOf<FileVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_file, parent,false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<FileVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}