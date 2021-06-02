package com.vk.videodownloader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException


class MainViewModel : ViewModel(){

    val saveVideoRequest : MutableLiveData<Call> = MutableLiveData()

    fun saveVideo(url: String) {
        viewModelScope.launch {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "video_file", "videoplayback.mp4",
                    File("/home/mixoz/AndroidStudioProjects/VkVideoDownloader/Vk-video-downloader/app/sampledata/videoplayback.mp4")
                        .asRequestBody("application/octet-stream".toMediaTypeOrNull())
                )
                .build()

            val request: Request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            saveVideoRequest.value = client.newCall(request)
        }
    }
}