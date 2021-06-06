package com.vk.videodownloader.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.videodownloader.AddVideoActivity
import com.vk.videodownloader.R
import com.vk.videodownloader.adapters.UploadingAdapter
import com.vk.videodownloader.constants.Constants
import com.vk.videodownloader.constants.Constants.Companion.isPauseOnBackground

class UploadingFragment : Fragment() {
    private lateinit var uploadingRV: RecyclerView
    private lateinit var uploadingAdapter: UploadingAdapter


    private fun createList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        uploadingRV.layoutManager = linearLayoutManager
        uploadingAdapter = UploadingAdapter()
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
        specification.setOnClickListener{
            isPauseOnBackground = specification.isChecked
        }
        createList()
        return view
    }

    override fun onStop() {
        Constants.isOnBackground--
        super.onStop()
    }

    override fun onStart() {
        uploadingAdapter.notifyDataSetChanged()
        Constants.isOnBackground++
        super.onStart()
    }
}