package com.vk.videodownloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.videodownloader.common.Common
import com.vk.videodownloader.common.Common.Companion.isCreated
import com.vk.videodownloader.common.Common.Companion.uploadedVideos
import com.vk.videodownloader.common.Common.Companion.uploadingVideos
import com.vk.videodownloader.common.getVideosFromUploadingVideos
import com.vk.videodownloader.data.Uploader
import com.vk.videodownloader.data.Video
import com.vk.videodownloader.serialization.JSONHelper


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_for_menu)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onAttachedToWindow() {
        if (!isCreated) {
            uploadedVideos = JSONHelper.importFromJSON(this, Common.Companion.Type.UPLOADED)
            val tmpUploadingVideos =
                JSONHelper.importFromJSON(this, Common.Companion.Type.UPLOADING)
            for (video: Video in tmpUploadingVideos) {

                uploadingVideos.add(
                    Uploader(
                        video,
                        null
                    )
                )
            }
        }
        isCreated = true

        super.onAttachedToWindow()
    }

    override fun onStop() {
        Common.isOnBackground--
        super.onStop()
    }

    override fun onStart() {
        Common.isOnBackground++
        super.onStart()
    }

    override fun onDestroy() {
        JSONHelper.exportToJSON(this, uploadedVideos, Common.Companion.Type.UPLOADED)
        JSONHelper.exportToJSON(
            this,
            getVideosFromUploadingVideos(),
            Common.Companion.Type.UPLOADING
        )
        super.onDestroy()
    }
}