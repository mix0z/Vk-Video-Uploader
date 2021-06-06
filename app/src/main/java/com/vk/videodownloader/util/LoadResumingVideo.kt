package com.vk.videodownloader.util

import com.vk.videodownloader.data.Uploader

interface LoadResumingVideo {
    fun onClicked(video: Uploader)
}
