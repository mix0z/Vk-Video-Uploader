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
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileInputStream
import java.io.IOException
import java.net.SocketException
import java.util.*


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

//            Thread {
//                val response_sb = StringBuilder()
//                try {
//                    val multipart = MultipartUtility(url, "UTF-8")
//                    multipart.addFilePart("video_file", FileInputStream(
//                        this.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
//                    ), getFileName(uri)!!)
//                    val response: List<String?> = multipart.finish()
//                    for (line in response) {
//                        response_sb.append(line)
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//                Log.d("AAAAAAAaa", response_sb.toString())
//            }.start()


//            val content = InputStreamRequestBody(
//                "video/mp4".toMediaType(), contentResolver,
//                uri
//            )

            val uuid = UUID.randomUUID().toString()
            val inputStream = FileInputStream(
                this.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
            )

            val file1 = ByteArray(4000000)
            inputStream.read(file1)
//            val file2 = ByteArray(inputStream.available())
//            inputStream.read(file2)
            inputStream.close()

            Thread {
                val client = OkHttpClient()


                val requestBody1: MultipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(
                        headersOf(
                            "Content-Disposition", "form-data; name=\"" + "video_file"
                                    + "\"; filename=\"" + getFileName(uri) + "\""
                        ),
                        file1.toRequestBody("video/mp4".toMediaType(), 0, file1.size)
                    )
                    .build()

                Log.d("vf", requestBody1.toString())

                val request1: Request = Request.Builder()
                    .addHeader("Session-ID", uuid)
                    //.addHeader("Content-Range", "bytes " + "0-" + (file1.size - 1).toString() + "/" + (file1.size + file2.size).toString())
                    .url(url)
                    .post(requestBody1)
                    .build()

                // Log.d("AAAAAAAAAa", request1.header("Content-Range")!!)
                //Log.d("AAAAAAAAAa", request1.header("Session-ID")!!)

                val response = client.newCall(request1).execute()

                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Log.d("SSSSSSSSSSSss", response.body!!.string())

                response.close()

                client.newCall(request1).execute().use {
                    if (!it.isSuccessful) throw IOException("Unexpected code $it")

                    Log.d("SSSSSSSSSSSss", it.body!!.string())
                    //it.close()
                }
            }.start()
//            Thread.sleep(3000)
//            Thread {
//                val client2 = OkHttpClient()
//
//                val requestBody2: MultipartBody = MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addPart(
//                        headersOf(
//                            "Content-Disposition", "form-data; name=\"" + "video_file"
//                                    + "\"; filename=\"" + getFileName(uri) + "\""
//                        ),
//                        file2.toRequestBody("video/mp4".toMediaType(), 0, file2.size)
//                    )
//                    .build()
//
//                Log.d("vf", requestBody2.toString())
//
//                val request2: Request = Request.Builder()
//                    .addHeader("Session-ID", uuid)
//                    //.addHeader("Content-Range", "bytes " + (file1.size).toString() + "-" + (file1.size + file2.size - 1).toString() + "/" + (file1.size + file2.size).toString())
//                    .url(url)
//                    .post(requestBody2)
//                    .build()
//
//                //Log.d("AAAAAAAAAa", request2.header("Content-Range")!!)
//                Log.d("AAAAAAAAAa", request2.header("Session-ID")!!)
//                try {
//                client2.newCall(request2).execute().use {
//                    if (!it.isSuccessful) throw IOException("Unexpected code $it")
//
//                    Log.d("SSSSSSSSSSSss", it.body!!.string())
//                    it.close()
//                }} catch (e : SocketException) {
//                    Log.d("wgregetgrgergergergerge", e.message.toString())
//                    Log.d("wgregetgrgergergergerge", e.cause.toString())
//                }
//            }.start()


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