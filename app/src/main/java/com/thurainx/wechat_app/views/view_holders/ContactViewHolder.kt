package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.delegate.ContactDelegate
import kotlinx.android.synthetic.main.view_holder_contact.view.*

class ContactViewHolder(itemView: View, delegate: ContactDelegate) : RecyclerView.ViewHolder(itemView) {

    var mContact : ContactVO? = null

    init {
        itemView.setOnClickListener {
            mContact?.let {
                delegate.onTapContact(it)
            }
        }
    }

    fun bind(contactVO: ContactVO){
        mContact = contactVO

        itemView.tvContactName.text = contactVO.name

        Glide.with(itemView.context)
            .load(contactVO.photoUrl)
            .into(itemView.ivContactProfile)
    }
}