package com.vk.videodownloader

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.sdk.api.video.VideoService
import com.vk.sdk.api.video.dto.VideoSaveResult
import com.vk.videodownloader.Constants.Companion.PICK_VIDEO


class MainActivity : AppCompatActivity() {
    lateinit var uri: Uri
    lateinit var url: String
    private var videoUploader: VideoUploader? = null
    var isPauseOnBackground: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addOnClickListenerForText()

        addOnClickListenerForText2()

        addOnClickListenerForButton()

        VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.VIDEO))
    }

    override fun onStop() {
        if (isPauseOnBackground) {
            Thread {
                videoUploader?.pause()
            }.start()
        }
        super.onStop()
    }

    override fun onStart() {
        if (isPauseOnBackground) {
            Thread {
                videoUploader?.resume()
            }.start()
        }
        super.onStart()
    }

    private fun addOnClickListenerForButton() {
        val button = findViewById<View>(R.id.button) as TextView
        button.setOnClickListener {
            videoUploader =
                VideoUploader(applicationContext.contentResolver.openInputStream(uri)!!, url)
            Thread {
                videoUploader!!.upload()
            }.start()

            val pause = findViewById<View>(R.id.pause) as TextView
            pause.setOnClickListener {
                Thread {
                    videoUploader!!.pause()
                }.start()
            }

            val resume = findViewById<View>(R.id.resume) as TextView
            resume.setOnClickListener {
                Thread {
                    videoUploader!!.resume()
                }.start()
            }
        }

    }

    private fun addOnClickListenerForText2() {
        val text2 = findViewById<View>(R.id.text2) as TextView
        text2.setOnClickListener {
            pickVideoIntent()
        }
    }

    private fun addOnClickListenerForText() {
        val text = findViewById<View>(R.id.text) as TextView
        text.setOnClickListener {
            VK.execute(VideoService().videoSave(
                "A.mp4",
                "A",
                false,
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                true,
                true
            ), object :
                VKApiCallback<VideoSaveResult> {
                override fun success(result: VideoSaveResult) {
                    url = result.uploadUrl!!
                    text.text = url
                }

                override fun fail(error: Exception) {
                    Log.e(TAG, error.toString())
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                val text = findViewById<View>(R.id.text) as TextView
                text.text = ("successful").toString()
            }

            override fun onLoginFailed(errorCode: Int) {
                val text = findViewById<View>(R.id.text) as TextView
                text.text = ("not successful").toString()
            }
        }

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_VIDEO) {
                if (resultCode == RESULT_OK) {
                    uri = data?.data!!
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You didn't pick video, try again.",
                        LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun pickVideoIntent() {
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO)
    }
}