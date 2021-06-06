package com.vk.videodownloader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vk.videodownloader.R
import com.vk.videodownloader.common.Common.Companion.uploadedVideos
import com.vk.videodownloader.listeners.VideoOnClickListener
import com.vk.videodownloader.data.Video

class UploadedAdapter(private val onClickListener: VideoOnClickListener) : RecyclerView.Adapter<UploadedAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val uploadedName: TextView = view.findViewById<View>(R.id.uploadedName) as TextView
        val uploadedDate: TextView = view.findViewById<View>(R.id.uploadedDate) as TextView
        val uploadedSize: TextView = view.findViewById<View>(R.id.uploadedSize) as TextView
        val deleteButton: ImageButton = view.findViewById<View>(R.id.deleteButton) as ImageButton
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.uploaded_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val video : Video = uploadedVideos[position]
        holder.uploadedName.text = video.name
        holder.uploadedName.setOnClickListener {
            onClickListener.onClicked(video)
        }
        holder.uploadedDate.text = video.date
        holder.uploadedSize.text = video.size.toString()

        holder.deleteButton.setOnClickListener {
            uploadedVideos.remove(video)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return uploadedVideos.size
    }
}