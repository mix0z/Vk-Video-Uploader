package com.vk.videodownloader.data

import android.net.Uri

class Video(
    var name: String? = null,
    var size: Int,
    var uploadedSize: Int,
    var date: String,
    var uri: Uri,
    var url: String
) {
}