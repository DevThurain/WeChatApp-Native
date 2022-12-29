package com.thurainx.wechat_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.data.vos.ContactGroupVO
import com.thurainx.wechat_app.delegate.ContactDelegate
import com.thurainx.wechat_app.delegate.ContactSelectDelegate
import com.thurainx.wechat_app.views.view_holders.ContactGroupViewHolder

class ContactGroupAdapter(val type: Int, val contactDelegate: ContactDelegate, val contactSelectDelegate: ContactSelectDelegate) : RecyclerView.Adapter<ContactGroupViewHolder>() {
    var mDataList = listOf<ContactGroupVO>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_contact_group, parent,false)
        return ContactGroupViewHolder(view, type, contactDelegate, contactSelectDelegate)
    }

    override fun onBindViewHolder(holder: ContactGroupViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setNewData(dataList: List<ContactGroupVO>){
        mDataList = dataList
        notifyDataSetChanged()
    }
}