package com.thurainx.wechat_app.views.view_holders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.SubtitleView.ViewType
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.delegate.ContactDelegate
import com.thurainx.wechat_app.delegate.ContactSelectDelegate
import com.thurainx.wechat_app.utils.VIEW_TYPE_SELECT
import kotlinx.android.synthetic.main.view_holder_contact.view.*

class ContactViewHolder(itemView: View,val viewType: Int,contactDelegate: ContactDelegate, contactSelectDelegate: ContactSelectDelegate) : RecyclerView.ViewHolder(itemView) {

    var mContact : ContactVO? = null

    init {
        itemView.setOnClickListener {
            mContact?.let {
                contactDelegate.onTapContact(it)
            }
        }

        itemView.cbContact.setOnCheckedChangeListener { view, isCheck ->
            mContact?.let {
                contactSelectDelegate.onSelectContact(isCheck,it)
            }
        }
    }

    fun bind(contactVO: ContactVO){
        mContact = contactVO

        itemView.tvContactName.text = contactVO.name

        Glide.with(itemView.context)
            .load(contactVO.photoUrl)
            .into(itemView.ivContactProfile)

        Log.d("viewType",viewType.toString())
        if(viewType == VIEW_TYPE_SELECT){
            itemView.cbContact.visibility = View.VISIBLE
        }else{
            itemView.cbContact.visibility = View.GONE
        }
    }
}