package com.vk.videodownloader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vk.videodownloader.LoadResumingVideo
import com.vk.videodownloader.R
import com.vk.videodownloader.common.Common.Companion.uploadingVideos
import com.vk.videodownloader.data.Uploader

class UploadingAdapter(private val onClickListener: LoadResumingVideo) : RecyclerView.Adapter<UploadingAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val uploadingName: TextView = view.findViewById<View>(R.id.uploadingName) as TextView
        val progressBar: ProgressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
        val cancelButton: ImageButton = view.findViewById<View>(R.id.cancelButton) as ImageButton
        val pauseResumeButton: ImageButton = view.findViewById<View>(R.id.pauseResumeButton) as ImageButton
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.uploading_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val video : Uploader = uploadingVideos[position]
        holder.uploadingName.text = video.video.name
//        holder.uploadingName.setOnClickListener {
//            onClickListener.onClicked(video.video)
//        }

        if (video.uploader == null) {
            holder.progressBar.visibility = INVISIBLE
            holder.pauseResumeButton.setOnClickListener {
                onClickListener.onClicked(video)
            }
        } else {
            holder.progressBar.visibility = VISIBLE
            holder.pauseResumeButton.setOnClickListener(null)
            if (video.uploader!!.getIsOnPause()) {
                holder.pauseResumeButton.setImageResource(R.drawable.outline_play_arrow_black_36)
            } else {
                holder.pauseResumeButton.setImageResource(R.drawable.outline_pause_black_36)
            }

            holder.pauseResumeButton.setOnClickListener {
                if (!video.uploader!!.getIsOnPause()) {
                    holder.pauseResumeButton.setImageResource(R.drawable.outline_play_arrow_black_36)
                    Thread {
                        video.uploader!!.pause()
                    }.start()
                } else {
                    holder.pauseResumeButton.setImageResource(R.drawable.outline_pause_black_36)
                    Thread {
                        video.uploader!!.resume()
                    }.start()
                }
            }

            holder.cancelButton.setOnClickListener {
                if (!video.uploader!!.getIsOnPause()) {
                    Thread {
                        video.uploader!!.pause()
                    }.start()
                }
                uploadingVideos.remove(video)
                notifyDataSetChanged()
            }
            holder.progressBar.progress = video.video.uploadedSize * 100 / video.video.size
            video.uploader!!.progressBar = holder.progressBar
            video.uploader!!.uploader = video
        }
    }

    override fun getItemCount(): Int {
        return uploadingVideos.size
    }
}