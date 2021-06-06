package com.vk.videodownloader.data

import com.vk.videodownloader.util.VideoUploader

data class Uploader(
    var video: Video,
    var uploader: VideoUploader?
)
