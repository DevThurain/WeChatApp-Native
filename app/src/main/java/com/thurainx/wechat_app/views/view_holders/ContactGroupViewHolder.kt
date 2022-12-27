package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.adapters.ContactAdapter
import com.thurainx.wechat_app.data.vos.ContactGroupVO
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.delegate.ContactDelegate
import kotlinx.android.synthetic.main.view_holder_contact_group.view.*

class ContactGroupViewHolder(itemView: View,val delegate: ContactDelegate) : RecyclerView.ViewHolder(itemView) {
    lateinit var mContactAdapter: ContactAdapter

    fun bind(contactGroupVO: ContactGroupVO){
        itemView.tvContactGroupName.text = contactGroupVO.symbol
        setUpRecyclerView(contactGroupVO.contactList)
    }

    private fun setUpRecyclerView(contactList: List<ContactVO>){
        mContactAdapter = ContactAdapter(delegate)
        itemView.rvContact.adapter = mContactAdapter
        mContactAdapter.setNewData(contactList)
    }
}