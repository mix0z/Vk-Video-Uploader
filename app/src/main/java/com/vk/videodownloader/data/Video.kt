package com.vk.videodownloader.data

data class Video(
    var name: String? = null,
    var description: String? = null,
    var isPrivate: Boolean? = null,
    var wallpost: Boolean? = null,
    var link: String? = null,
    var groupId: Int? = null,
    var albumId: Int? = null,
    var privacyView: List<String>? = null,
    var privacyComment: List<String>? = null,
    var noComments: Boolean? = null,
    var repeat: Boolean? = null,
    var compression: Boolean? = null,
    var size: Long,
    var uploadedSize: Long,
    var date: String
)
