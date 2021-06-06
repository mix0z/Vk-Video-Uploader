package com.vk.videodownloader.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.videodownloader.AddVideoActivity
import com.vk.videodownloader.R
import com.vk.videodownloader.adapters.UploadingAdapter
import com.vk.videodownloader.common.Common
import com.vk.videodownloader.common.Common.Companion.isPauseOnBackground
import com.vk.videodownloader.common.getVideosFromUploadingVideos
import com.vk.videodownloader.data.Uploader
import com.vk.videodownloader.serialization.JSONHelper
import com.vk.videodownloader.util.LoadResumingVideo
import com.vk.videodownloader.util.VideoUploader

class UploadingFragment : Fragment() {
    private lateinit var uploadingRV: RecyclerView
    private lateinit var uploadingAdapter: UploadingAdapter
    private lateinit var uri: Uri
    private lateinit var video: Uploader

    private fun pickVideoIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "video/*"
        startActivityForResult(intent, Common.PICK_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Common.PICK_VIDEO) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                uri = data?.data!!

                Toast.makeText(
                    context,
                    "You picked video, uploading will continue from where it left off.",
                    Toast.LENGTH_LONG
                ).show()

                val inputStream =
                    context?.applicationContext?.contentResolver?.openInputStream(uri)!!
                val tmpSize = inputStream.available() - video.video.uploadedSize.toLong() + 208
                while (inputStream.available().toLong() != tmpSize) {
                    inputStream.skip(inputStream.available() - tmpSize)
                }
                video.uploader =
                    VideoUploader(
                        inputStream, video.video.url, context?.contentResolver?.getType(uri)
                            .toString()
                    )
                video.uploader!!.leftRange = video.video.uploadedSize
                video.uploader!!.size += video.video.uploadedSize - 208

                uploadingAdapter.notifyDataSetChanged()

            } else {
                Toast.makeText(
                    context,
                    "You didn't pick video, try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        uploadingRV.layoutManager = linearLayoutManager
        uploadingAdapter = UploadingAdapter(object : LoadResumingVideo {
            override fun onClicked(video: Uploader) {
                this@UploadingFragment.video = video
                pickVideoIntent()
            }
        })
        uploadingRV.adapter = uploadingAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_uploading, container, false)
        uploadingRV = view.findViewById(R.id.uploadingRV)
        val addButton = view.findViewById<ImageButton>(R.id.addButton)
        addButton.setOnClickListener {
            startActivity(Intent(context, AddVideoActivity::class.java))
            uploadingAdapter.notifyDataSetChanged()
        }
        val specification = view.findViewById<SwitchCompat>(R.id.specification)
        specification.isChecked = isPauseOnBackground
        specification.setOnClickListener {
            isPauseOnBackground = specification.isChecked
        }
        createList()
        return view
    }

    override fun onStop() {
        Common.isOnBackground--
        super.onStop()
    }

    override fun onStart() {
        uploadingAdapter.notifyDataSetChanged()
        Common.isOnBackground++
        super.onStart()
    }

    override fun onDestroy() {
        JSONHelper.exportToJSON(
            requireContext(),
            Common.uploadedVideos,
            Common.Companion.Type.UPLOADED
        )
        JSONHelper.exportToJSON(
            requireContext(),
            getVideosFromUploadingVideos(),
            Common.Companion.Type.UPLOADING
        )
        super.onDestroy()
    }
}