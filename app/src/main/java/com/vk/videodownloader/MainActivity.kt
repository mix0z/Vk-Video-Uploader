package com.vk.videodownloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_for_menu)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onStop() {
        //TODO make it for all
//        if (isPauseOnBackground) {
//            Thread {
//                videoUploader?.pause()
//            }.start()
//        }
        super.onStop()
    }

    override fun onStart() {
//        if (isPauseOnBackground) {
//            Thread {
//                videoUploader?.resume()
//            }.start()
//        }
        super.onStart()
    }
}