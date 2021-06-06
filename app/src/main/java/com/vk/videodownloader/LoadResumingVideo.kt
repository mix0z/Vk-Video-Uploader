package com.vk.videodownloader

import com.vk.videodownloader.data.Uploader

interface LoadResumingVideo {
    fun onClicked(video: Uploader)
}
