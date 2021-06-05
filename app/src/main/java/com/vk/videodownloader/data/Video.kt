package com.vk.videodownloader.data

data class Video(
    var name: String? = null,
    var description: String? = null,
    var isPrivate: Boolean? = null,
    var wallpost: Boolean? = null,
    var repeat: Boolean? = null,
    var compression: Boolean? = null,
    var size: Int,
    var uploadedSize: Int,
    var date: String
)
