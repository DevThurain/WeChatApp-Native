package com.thurainx.wechat_app.views.view_holders

import android.graphics.Bitmap
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.view_holder_file.view.*
import kotlinx.android.synthetic.main.view_holder_smart_image.view.*


class SmartImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(image: String){

//        Glide.with(itemView.context)
//            .load(image)
//            .into(itemView.ivSmartImage)


        Log.d("my_photo_list",image)

        Glide.with(itemView.context)
            .asBitmap()
            .load(image)
            .into(object : SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val width = resource.width
                    val height = resource.height

                    if(width > height){
                        val dimensionInDp = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            350F,
                            itemView.context.resources.displayMetrics
                        ).toInt()

                        itemView.ivSmartImage.layoutParams.width = dimensionInDp
                        itemView.ivSmartImage.requestLayout()
//                          itemView.ivSmartImage.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT - dimensionInDp
//                          itemView.ivSmartImage.requestLayout()
                    }else{

                    }

                    itemView.ivSmartImage.setImageBitmap(resource)

                }
            })
    }
}