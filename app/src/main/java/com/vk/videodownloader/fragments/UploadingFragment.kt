package com.vk.videodownloader.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vk.videodownloader.R
import com.vk.videodownloader.adapters.UploadedAdapter
import com.vk.videodownloader.adapters.UploadingAdapter
import com.vk.videodownloader.data.Video
import com.vk.videodownloader.listeners.VideoOnClickListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var uploadingRV: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun createList() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        uploadingRV.layoutManager = linearLayoutManager
        val uploaded = ArrayList<Video>()
        uploaded.add(
            Video(
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
                true,
                1245364,
                38606,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
                "B.mp4",
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
                true,
                1245364,
                1000000,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
                "C.mp4",
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
                true,
                1245364,
                1245364,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
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
                true,
                1245364,
                6000000,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
                "B.mp4",
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
                true,
                1245364,
                2345,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
                "C.mp4",
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
                true,
                1245364,
                26,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
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
                true,
                1245364,
                435454,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
                "B.mp4",
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
                true,
                1245364,
                345534,
                "2020-04-04",
            )
        )
        uploaded.add(
            Video(
                "C.mp4",
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
                true,
                1245364,
                4345,
                "2020-04-04",
            )
        )
        val adapter = UploadingAdapter(uploaded, object : VideoOnClickListener {
            override fun onClicked(uploaded: Video) {
                Log.d("Click", uploaded.name!!)
            }

        })
        uploadingRV.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_uploading, container, false)
        uploadingRV = view.findViewById(R.id.uploadingRV)
        createList()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UploadingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}