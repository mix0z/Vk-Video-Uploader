package com.vk.videodownloader

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.*

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 * @author www.codejava.net
 */
class MultipartUtility(
    private val requestURL: String?,
    private val charset: String,
    private val localInputStream: FileInputStream
) {
    private val boundary: String = "===" + System.currentTimeMillis() + "==="
    private lateinit var httpConn: HttpURLConnection
    private lateinit var outputStream: OutputStream
    private lateinit var writer: PrintWriter
    private val uuid: String = UUID.randomUUID().toString()
    private var beginPosition: Int = 0
    private val fileSize: Int = localInputStream.available()
    private val buffer = ByteArray(5000)


    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..."></input>
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    @Throws(IOException::class)
    fun addFilePart(fieldName: String, fileName: String) {
        var bytesRead: Int
        (localInputStream.read(buffer).also { bytesRead = it })

//        if (bytesRead == -1) {
//            return
//        }

        addHeaderField(
            "Content-Range",
            "bytes $beginPosition-" + (beginPosition + bytesRead - 1).toString() + "/" + fileSize
        )
        beginPosition+= bytesRead

        writer.append("--$boundary").append(LINE_FEED)
        writer.append(
            "Content-Disposition: form-data; name=\"" + fieldName
                    + "\"; filename=\"" + fileName + "\""
        )
            .append(LINE_FEED)
        writer.append(
            (
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
        )
            .append(LINE_FEED)
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED)
        writer.append(LINE_FEED)
        writer.flush()

        outputStream.write(buffer, 0, bytesRead)

        outputStream.flush()
        writer.append(LINE_FEED)
        writer.flush()
    }

    fun addFileRange(fieldName: String, fileName: String) {
        do {
            //Log.d("QQQQQQQQQQQQQQq", localInputStream.available().toString())
            initNewRequest()
            addFilePart(fieldName, fileName)
            finish()
        } while (fileSize > beginPosition)
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    fun addHeaderField(name: String, value: String) {
        writer.append("$name: $value").append(LINE_FEED)
        writer.flush()
        Log.d("PPPPPPPPPPPPPPPP", value)
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun finish(): List<String?> {
        val response: MutableList<String?> = ArrayList()
        writer.append(LINE_FEED).flush()
        writer.append("--$boundary--").append(LINE_FEED)
        writer.close()

        // checks server's status code first
        val status = httpConn.responseCode
        if (status == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(
                InputStreamReader(
                    httpConn.inputStream
                )
            )
            var line: String? = null
            while ((reader.readLine().also { line = it }) != null) {
                response.add(line)
            }
            reader.close()
            httpConn.disconnect()
        } else {
            throw IOException("Server returned non-OK status: $status")
        }
        val response_sb = StringBuilder()
        for (line in response) {

            response_sb.append(line)
                    }
        Log.d("AAAAAAAAAAAAAA", response_sb.toString())
        return response
    }

    companion object {
        private val LINE_FEED = "\r\n"
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    private fun initNewRequest() {
        val url = URL(requestURL)
        httpConn = url.openConnection() as HttpURLConnection
        httpConn.useCaches = false
        httpConn.doOutput = true // indicates POST method
        httpConn.doInput = true
        httpConn.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=$boundary"
        )
        outputStream = httpConn.outputStream
        writer = PrintWriter(
            OutputStreamWriter(outputStream, charset),
            true
        )
        addHeaderField("Session-ID", uuid)
    }
}