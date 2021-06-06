package com.vk.videodownloader.common

import com.vk.videodownloader.data.Uploader
import com.vk.videodownloader.data.Video

class Common {
    companion object{
        const val BUFFER_SIZE = 2048
        const val PICK_VIDEO = 1
        var uploadingVideos = ArrayList<Uploader>()
        var uploadedVideos = ArrayList<Video>()
        var isPauseOnBackground: Boolean = false
        var isOnBackground: Int = 0
        var isCreated:Boolean = false

        enum class Type {
            UPLOADING,
            UPLOADED
        }
    }
}

fun getVideosFromUploadingVideos() : ArrayList<Video> {
    val videos: ArrayList<Video> = ArrayList()
    for (uploader : Uploader in Common.uploadingVideos){
        videos.add(uploader.video)
    }
    return videos
}