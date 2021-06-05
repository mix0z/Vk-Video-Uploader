package com.vk.videodownloader.constants

import com.vk.videodownloader.data.Uploader
import com.vk.videodownloader.data.Video

class Constants {
    companion object{
        const val BUFFER_SIZE = 2048
        const val PICK_VIDEO = 1
        var uploadingVideos = ArrayList<Uploader>()
        var uploadedVideos = ArrayList<Video>()
        var isPauseOnBackground: Boolean = false
    }
}