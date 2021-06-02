package com.vk.videodownloader

import android.content.ContentValues.TAG
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.sdk.api.video.VideoService
import com.vk.sdk.api.video.dto.VideoSaveResult
import okhttp3.*
import java.io.FileInputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    lateinit var uri: Uri
    lateinit var url: String
    private val PICK_VIDEO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewModel = MainViewModel()

        addOnClickListenerForText()

        addOnClickListenerForText2()

        addOnClickListenerForButton()

        VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.VIDEO))

        //VK.addTokenExpiredHandler(tokenTracker)


    }

//    fun getPath(uri: Uri?): String? {
//        val projection = arrayOf(MediaStore.Video.Media.DATA)
//        val cursor: Cursor? = contentResolver.query(uri!!, projection, null, null, null)
//        return if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            val column_index: Int = cursor
//                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
//            cursor.moveToFirst()
//            cursor.getString(column_index)
//        } else null
//    }

    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor.use { c ->
                if (c != null && c.moveToFirst()) {
                    result = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result
    }

    private fun addOnClickListenerForButton() {
        val button = findViewById<View>(R.id.button) as TextView
        button.setOnClickListener {
            Log.d("BUTTON", "SSSSSSSSSSSSSSSSSSSSSSSs")

            Thread {
                val response_sb = StringBuilder()
                try {
                    val multipart = MultipartUtility(url, "UTF-8")
                    multipart.addFilePart("video_file", FileInputStream(
                        this.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
                    ), getFileName(uri)!!)
                    val response: List<String?> = multipart.finish()
                    for (line in response) {
                        response_sb.append(line)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                Log.d("AAAAAAAaa", response_sb.toString())
            }.start()


//            val content = InputStreamRequestBody(
//                "video/mp4".toMediaType(), contentResolver,
//                uri
//            )
//
//
//
//            val requestBody: RequestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart(
//                    "video_file", "A.mp4", content
//                )
//                .build()
//
//            val request: Request = Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build()
//
//            val client = OkHttpClient()
//            client.newCall(request).enqueue(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    //text.text = ("not successful").toString()
//                    Log.d("not successful", e.toString())
//                    Log.d("not successful", e.message.toString())
//                    Log.d("not successful", call.toString())
//                    e.printStackTrace()
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    //text.text = ("successful").toString()
//                    Log.d("successful", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
//                }
//            })
        }
    }

    private fun addOnClickListenerForText2() {
        val text2 = findViewById<View>(R.id.text2) as TextView
        text2.setOnClickListener {
            Log.d("TEXT 2", "SSSSSSSSSSSSSSSSSSSSSSSs")
            pickVideoIntent()
        }
    }

    private fun addOnClickListenerForText() {
        val text = findViewById<View>(R.id.text) as TextView
        text.setOnClickListener {
            VK.execute(VideoService().videoSave(
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
                true
            ), object :
                VKApiCallback<VideoSaveResult> {
                override fun success(result: VideoSaveResult) {
                    url = result.uploadUrl!!

                    text.text = url

                    Log.d("URL : ", url)
                }

                override fun fail(error: Exception) {
                    Log.e(TAG, error.toString())
                }
            })
        }

        /*
         * Получаем адрес для загрузки видео
         */
    }

//    private val tokenTracker = object: VKTokenExpiredHandler {
//        override fun onTokenExpired() {
//            // token expired
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val callback = object : VKAuthCallback {
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
            if (requestCode == PICK_VIDEO) {
                if (resultCode == RESULT_OK) {
                    Log.d("XXXXXXXXXXXXXXXXXXXXXxx", data?.data?.encodedPath.toString())
                    uri = data?.data!!
                } else {
                    Log.d("FFFFFFFFFFFFFFFFFFFFfff", resultCode.toString())
                }
            }
        }
    }

    private fun pickVideoIntent() {
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO)
    }
}