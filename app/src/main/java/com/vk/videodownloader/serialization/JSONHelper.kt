package com.vk.videodownloader.serialization

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.*
import com.vk.videodownloader.common.Common
import com.vk.videodownloader.data.Video
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


class UriInOut : JsonSerializer<Uri>, JsonDeserializer<Uri> {

    override fun serialize(
        src: Uri,
        typeOfSrc: java.lang.reflect.Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString());
    }

    override fun deserialize(
        src: JsonElement, srcType: java.lang.reflect.Type,
        context: JsonDeserializationContext
    ): Uri {
        return Uri.parse(src.asString);
    }
}


internal object JSONHelper {
    private const val UPLOADED_FILE_NAME = "uploaded.json"
    private const val UPLOADING_FILE_NAME = "uploading.json"
    fun exportToJSON(
        context: Context,
        dataList: ArrayList<Video>,
        type: Common.Companion.Type
    ): Boolean {
        val gson =  GsonBuilder()
            .registerTypeAdapter(Uri::class.java, UriInOut())
            .create();
        val dataItems = DataItems()
        dataItems.videos = dataList
        val jsonString = gson.toJson(dataItems)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = if (type == Common.Companion.Type.UPLOADED) {
                context.openFileOutput(UPLOADED_FILE_NAME, Context.MODE_PRIVATE)
            } else {
                context.openFileOutput(UPLOADING_FILE_NAME, Context.MODE_PRIVATE)
            }
            fileOutputStream.write(jsonString.toByteArray())
            Log.d("Serialize", "Success" + type.name)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        Log.d("Serialize", "Failed" + type.name)
        return false
    }

    fun importFromJSON(context: Context, type: Common.Companion.Type): ArrayList<Video> {
        var streamReader: InputStreamReader? = null
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = if (type == Common.Companion.Type.UPLOADED) {
                context.openFileInput(UPLOADED_FILE_NAME)
            } else {
                context.openFileInput(UPLOADING_FILE_NAME)
            }
            streamReader = InputStreamReader(fileInputStream)
            val gson = GsonBuilder()
                .registerTypeAdapter(Uri::class.java, UriInOut())
                .create();
            val dataItems = gson.fromJson(streamReader, DataItems::class.java)
            return dataItems.videos
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return ArrayList()
    }

    private class DataItems {
        var videos: ArrayList<Video> = ArrayList()
    }
}
