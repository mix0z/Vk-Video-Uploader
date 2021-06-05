package com.vk.videodownloader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.video.VideoService
import com.vk.sdk.api.video.dto.VideoSaveResult
import com.vk.videodownloader.constants.Constants.Companion.PICK_VIDEO
import com.vk.videodownloader.constants.Constants.Companion.uploadingVideos
import com.vk.videodownloader.data.Uploader
import com.vk.videodownloader.data.Video
import com.vk.videodownloader.util.VideoUploader
import java.util.*

class AddVideoActivity : AppCompatActivity() {
    lateinit var uri: Uri
    lateinit var url: String

    private lateinit var private: SwitchCompat
    private lateinit var wallPost: SwitchCompat
    private lateinit var repeat: SwitchCompat
    private lateinit var compress: SwitchCompat

    private var isVideoPicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)

        addImages()
        addSwitchers()
        run()
    }

    private fun addImages() {
        val addVideo = findViewById<ImageButton>(R.id.addVideo)
        addVideo.setOnClickListener {
            pickVideoIntent()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO) {
            if (resultCode == RESULT_OK) {
                uri = data?.data!!
                isVideoPicked = true
                val addVideoButton = findViewById<ImageButton>(R.id.addVideo)
                addVideoButton.setImageResource(R.drawable.baseline_done_black_36)
            } else {
                Toast.makeText(
                    applicationContext,
                    "You didn't pick video, try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun pickVideoIntent() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO)
    }

    private fun addSwitchers() {
        private = findViewById(R.id.private_)
        wallPost = findViewById(R.id.wallpost)
        repeat = findViewById(R.id.repeat)
        compress = findViewById(R.id.compress)
    }

    private fun run() {
        val cancel = findViewById<Button>(R.id.cancel)
        cancel.setOnClickListener {
            onBackPressed()
        }
        val submit = findViewById<Button>(R.id.submit)
        val videoName = findViewById<EditText>(R.id.videoName)
        val videoDescription = findViewById<EditText>(R.id.videoDescription)
        submit.setOnClickListener {
            when {
                TextUtils.isEmpty(videoName.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@AddVideoActivity,
                        "Please enter name of the video.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                (!isVideoPicked) -> {
                    Toast.makeText(
                        this@AddVideoActivity,
                        "Please pick the video.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val name: String =
                        videoName.text.toString().trim { it <= ' ' }
                    val description: String = videoDescription.text.toString().trim { it <= ' ' }
                    VK.execute(
                        VideoService().videoSave(
                            name = name,
                            description = description,
                            isPrivate = private.isChecked,
                            wallpost = wallPost.isChecked,
                            repeat = repeat.isChecked,
                            compression = compress.isChecked
                        ), object :
                            VKApiCallback<VideoSaveResult> {
                            override fun success(result: VideoSaveResult) {
                                url = result.uploadUrl!!
                                val inputStream = applicationContext.contentResolver.openInputStream(uri)!!
                                val videoUploader =
                                    VideoUploader(inputStream, url)

                                uploadingVideos.add(
                                    Uploader(
                                        Video(
                                            name,
                                            description,
                                            private.isChecked,
                                            wallPost.isChecked,
                                            repeat.isChecked,
                                            compress.isChecked,
                                            inputStream.available(),
                                            0,
                                            Calendar.getInstance().time.toString(),
                                        ),
                                        videoUploader
                                    )
                                )
                                Thread{
                                    videoUploader.upload()
                                }.start()
                                startActivity(Intent(this@AddVideoActivity, MainActivity::class.java))
                            }

                            override fun fail(error: Exception) {
                                Toast.makeText(
                                    this@AddVideoActivity,
                                    "Something went wrong, try again later.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }
        }
    }
}