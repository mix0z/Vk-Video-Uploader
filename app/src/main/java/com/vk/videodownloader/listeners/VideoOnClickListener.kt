package com.vk.videodownloader.listeners

import com.vk.videodownloader.data.Video

interface VideoOnClickListener {
    fun onClicked(uploaded : Video)
}