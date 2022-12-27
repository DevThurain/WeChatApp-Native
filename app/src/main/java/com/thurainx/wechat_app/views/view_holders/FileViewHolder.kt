package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.delegate.FileDelegate
import kotlinx.android.synthetic.main.view_holder_file.view.*

class FileViewHolder(itemView: View, delegate: FileDelegate) : RecyclerView.ViewHolder(itemView) {
    var mFileVO : FileVO? = null

    init {
        itemView.ivFileRemove.setOnClickListener {
            mFileVO?.let {
                delegate.onTapDelete(it)
            }
        }
    }

    fun bind(fileVO: FileVO){
        mFileVO = fileVO
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