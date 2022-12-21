package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thurainx.wechat_app.data.vos.FileVO
import kotlinx.android.synthetic.main.view_holder_file.view.*

class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(fileVO: FileVO){

        if(fileVO.isMovie){
            itemView.ivPlayArrow.visibility = View.VISIBLE
        }else{
            itemView.ivPlayArrow.visibility = View.GONE
        }
        Glide.with(itemView.context)
            .load(fileVO.bitmap)
            .into(itemView.ivFileImage)
    }
}