package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thurainx.wechat_app.data.vos.ContactVO
import kotlinx.android.synthetic.main.view_holder_contact.view.*

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mContact : ContactVO? = null

    fun bind(contactVO: ContactVO){
        mContact = contactVO

        itemView.tvContactName.text = contactVO.name

        Glide.with(itemView.context)
            .load(contactVO.photoUrl)
            .into(itemView.ivContactProfile)
    }
}