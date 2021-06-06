package com.vk.videodownloader.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.api.sdk.VK
import com.vk.videodownloader.AuthorizationActivity
import com.vk.videodownloader.R
import com.vk.videodownloader.adapters.UploadedAdapter
import com.vk.videodownloader.common.Common
import com.vk.videodownloader.common.Common.Companion.isPauseOnBackground
import com.vk.videodownloader.common.Common.Companion.uploadedVideos
import com.vk.videodownloader.common.Common.Companion.uploadingVideos
import com.vk.videodownloader.common.getVideosFromUploadingVideos
import com.vk.videodownloader.data.Video
import com.vk.videodownloader.listeners.VideoOnClickListener
import com.vk.videodownloader.serialization.JSONHelper

class UploadedFragment : Fragment() {
    private lateinit var uploadedRV: RecyclerView
    private lateinit var uploadedAdapter: UploadedAdapter

    private fun createList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        uploadedRV.layoutManager = linearLayoutManager

        uploadedAdapter = UploadedAdapter(object : VideoOnClickListener {
            override fun onClicked(uploaded: Video) {
                Log.d("Click", uploaded.name!!)
            }

        })
        uploadedRV.adapter = uploadedAdapter


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_uploaded, container, false)
        uploadedRV = view.findViewById(R.id.uploadedRV)
        val logout = view.findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            uploadingVideos = ArrayList()
            uploadedVideos = ArrayList()
            isPauseOnBackground = true
            VK.logout()
            startActivity(Intent(context, AuthorizationActivity::class.java))
        }
        createList()
        return view
    }

    override fun onStop() {
        Common.isOnBackground--
        super.onStop()
    }

    override fun onStart() {
        uploadedAdapter.notifyDataSetChanged()
        Common.isOnBackground++
        super.onStart()
    }

    override fun onDestroy() {
        JSONHelper.exportToJSON(requireContext(), uploadedVideos, Common.Companion.Type.UPLOADED)
        JSONHelper.exportToJSON(requireContext(), getVideosFromUploadingVideos(), Common.Companion.Type.UPLOADING)
        super.onDestroy()
    }
}