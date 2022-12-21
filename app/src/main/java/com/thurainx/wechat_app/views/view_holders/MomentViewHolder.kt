package com.thurainx.wechat_app.views.view_holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.snov.timeagolibrary.PrettyTimeAgo
import com.thurainx.wechat_app.adapters.SmartImageAdapter
import com.thurainx.wechat_app.data.vos.MomentVO
import kotlinx.android.synthetic.main.view_holder_moment.view.*


class MomentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mSmartImageAdapter = SmartImageAdapter()

    fun bind(momentVO: MomentVO){
        itemView.tvMomentName.text = momentVO.name

        Glide.with(itemView.context)
            .load(momentVO.profileImage)
            .into(itemView.ivMomentProfile)

        itemView.tvMomentTime.text = PrettyTimeAgo.getTimeAgo(momentVO.millis)
        itemView.tvMomentText.text = momentVO.text.toString()

        if(momentVO.photoList.isNotEmpty()){
            itemView.rvMomentPhoto.visibility = View.VISIBLE
            itemView.rvMomentPhoto.adapter = mSmartImageAdapter
            mSmartImageAdapter.setNewData(momentVO.photoList)
        }else{
            itemView.rvMomentPhoto.visibility = View.GONE
        }

        if(momentVO.videoLink.isNotEmpty()){
            itemView.playerMoment.visibility = View.VISIBLE
            setUpPlayer(videoLink = momentVO.videoLink,itemView)
        }else{
            itemView.playerMoment.visibility = View.GONE
        }

    }

    private fun setUpPlayer(videoLink: String,itemView: View){
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(itemView.context)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(videoLink))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        val simpleExoPlayer = ExoPlayer.Builder(itemView.context)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        simpleExoPlayer.addMediaSource(mediaSource)

        simpleExoPlayer.playWhenReady = true
//        binding.playerView.player = simpleExoPlayer
//        binding.playerView.requestFocus()

        itemView.playerMoment.player = simpleExoPlayer
        itemView.playerMoment.requestFocus()
    }
}