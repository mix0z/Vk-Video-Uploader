package com.vk.videodownloader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.videodownloader.common.Common
import com.vk.videodownloader.common.getVideosFromUploadingVideos
import com.vk.videodownloader.serialization.JSONHelper

class AuthorizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        val login = findViewById<Button>(R.id.login)

        login.setOnClickListener {
            if (VK.isLoggedIn()) {
                startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
            } else {
                VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.VIDEO))
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                Log.d(
                    "aAAAaaAAAAAAAAAAAa",
                    JSONHelper.exportToJSON(
                        this@AuthorizationActivity,
                        Common.uploadedVideos,
                        Common.Companion.Type.UPLOADED
                    ).toString()
                )
                Log.d(
                    "aAAAaaAAAAAAAAAAAa",
                    JSONHelper.exportToJSON(
                        this@AuthorizationActivity,
                        getVideosFromUploadingVideos(),
                        Common.Companion.Type.UPLOADING
                    ).toString()
                )
                startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(
                    this@AuthorizationActivity,
                    "Authorization failed, try again",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}