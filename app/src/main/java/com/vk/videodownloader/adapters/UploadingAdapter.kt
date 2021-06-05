package com.vk.videodownloader.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vk.videodownloader.R
import com.vk.videodownloader.listeners.VideoOnClickListener
import com.vk.videodownloader.data.Video

class UploadingAdapter(private val uploading: ArrayList<Video>, private val onClickListener: VideoOnClickListener) : RecyclerView.Adapter<UploadingAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val uploadingName: TextView = view.findViewById<View>(R.id.uploadingName) as TextView
        val progressBar: ProgressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
        val cancelButton: ImageButton = view.findViewById<View>(R.id.cancelButton) as ImageButton
        val pauseResumeButton: ImageButton = view.findViewById<View>(R.id.pauseResumeButton) as ImageButton
    }

    private var isOnPause: Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.uploading_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val video : Video = uploading[position]
        holder.uploadingName.text = video.name
        holder.uploadingName.setOnClickListener {
            onClickListener.onClicked(video)
        }
        holder.pauseResumeButton.setOnClickListener {
            //TODO set on pause
            if (isOnPause) {
                isOnPause = false
                holder.pauseResumeButton.setImageResource(R.drawable.outline_play_arrow_black_36)
            } else {
                isOnPause = true
                holder.pauseResumeButton.setImageResource(R.drawable.outline_pause_black_36)
            }
        }

        holder.cancelButton.setOnClickListener {
            //TODO cancel and delete from recyclerview
        }
        holder.progressBar.progress = (video.uploadedSize * 100 / video.size).toInt()
    }

    override fun getItemCount(): Int {
        return uploading.size
    }
}