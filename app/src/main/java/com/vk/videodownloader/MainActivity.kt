package com.vk.videodownloader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.videodownloader.constants.Constants
import com.vk.videodownloader.constants.Constants.Companion.isOnBackground
import com.vk.videodownloader.constants.Constants.Companion.isPauseOnBackground


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_for_menu)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val navController = findNavController(R.id.fragmentContainerView)

        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onStop() {
        Constants.isOnBackground--
        super.onStop()
    }

    override fun onStart() {
        Constants.isOnBackground++
        super.onStart()
    }
}