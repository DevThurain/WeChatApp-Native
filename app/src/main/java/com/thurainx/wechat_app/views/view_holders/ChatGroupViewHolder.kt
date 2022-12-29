package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thurainx.wechat_app.adapters.ContactAdapter
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.GroupVO
import com.thurainx.wechat_app.delegate.GroupDelegate
import kotlinx.android.synthetic.main.view_holder_chat_group.view.*
import kotlinx.android.synthetic.main.view_holder_contact_group.view.*

class ChatGroupViewHolder(itemView: View, delegate: GroupDelegate) : RecyclerView.ViewHolder(itemView) {

    var mGroupVO : GroupVO? = null

    init {
        itemView.setOnClickListener {
            mGroupVO?.let {
                delegate.onTapGroup(it)
            }
        }
    }


    fun bind(groupVO: GroupVO){
        mGroupVO = groupVO

        Glide.with(itemView.context)
            .load(groupVO.photo)
            .into(itemView.ivGroupPhoto)

        itemView.tvGroupName.text = groupVO.name

    }


}