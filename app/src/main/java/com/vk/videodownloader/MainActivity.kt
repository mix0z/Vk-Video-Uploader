package com.vk.videodownloader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS))
        //VK.addTokenExpiredHandler(tokenTracker)
    }

//    private val tokenTracker = object: VKTokenExpiredHandler {
//        override fun onTokenExpired() {
//            // token expired
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
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
        }
    }
}