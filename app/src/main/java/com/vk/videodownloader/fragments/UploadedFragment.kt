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
import com.vk.videodownloader.AuthorizationActivity
import com.vk.videodownloader.R
import com.vk.videodownloader.adapters.UploadedAdapter
import com.vk.videodownloader.constants.Constants.Companion.isPauseOnBackground
import com.vk.videodownloader.constants.Constants.Companion.uploadedVideos
import com.vk.videodownloader.constants.Constants.Companion.uploadingVideos
import com.vk.videodownloader.data.Video
import com.vk.videodownloader.listeners.VideoOnClickListener

class UploadedFragment : Fragment() {
    private lateinit var uploadedRV: RecyclerView


    private fun createList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        uploadedRV.layoutManager = linearLayoutManager

        val adapter = UploadedAdapter(object : VideoOnClickListener {
            override fun onClicked(uploaded: Video) {
                Log.d("Click", uploaded.name!!)
            }

        })
        uploadedRV.adapter = adapter


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
            startActivity(Intent(context, AuthorizationActivity::class.java))
        }
        createList()
        return view
    }
}