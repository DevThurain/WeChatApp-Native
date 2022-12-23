package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.thurainx.wechat_app.delegate.ChatDelegate

class ActiveUserViewHolder(itemView: View,chatDelegate: ChatDelegate) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            chatDelegate.onTapChat()
        }
    }
    fun bind(temp: String){

    }
}