package com.kotlin.nehabaliassignmenttest.viewmodel

import android.app.Application
import android.app.DownloadManager
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DownloadFileViewModel(application: Application) : AndroidViewModel(application) {
    private var downloadManager: DownloadManager? = null

    init {
        downloadManager = application.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
    }

    fun dwonloadFile(
        destinationPath: File,
        fileName: String,
        downloadUrl: String,
        success: (String) -> Unit,
        failure: (String) -> Unit,

        ) {

        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(downloadUrl))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)  // Visibility of the download Notification
            .setDestinationUri(destinationPath.toUri()) // Uri of the destination file
            .setTitle(fileName) // Title of the Download Notification
            .setDescription("downloading......") // Description of the Download Notification
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI.or(DownloadManager.Request.NETWORK_MOBILE))
//            .addRequestHeader(Constants.VISION_HEADER_KEY, Constants.VISION_HEADER_VALUE)

        val downloadID = downloadManager!!.enqueue(request)

        viewModelScope.launch(Dispatchers.IO) {
//            downloadingStatus(fileName,downloadID, success, failure)
            beginDownload(downloadID, fileName, success, failure)
        }
//        CoroutineScope(Dispatchers.Default).launch {
//            beginDownload(downloadID, fileName, success, failure)
//
//        }

    }



    suspend fun beginDownload(
        downloadID: Long, fileName: String, success: (String) -> Unit,
        failure: (String) -> Unit,
    ) {

        // using query method
        var finishDownload = false
        var progress: Int
        val myDownloadQuery: DownloadManager.Query = DownloadManager.Query()
        //set the query filter to our previously Enqueued download
        myDownloadQuery.setFilterById(downloadID)
        //Query the download manager about downloads that have been requested.
        while (!finishDownload) {
            val cursor: Cursor = downloadManager!!.query(myDownloadQuery)

            if (cursor.moveToFirst()) {

                when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    DownloadManager.STATUS_FAILED -> {
                        finishDownload = true
                        withContext(Dispatchers.Main) {
                            failure.invoke(fileName)
                            Toast.makeText(getApplication(), "Download failed", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("asdasdadad", "Download failed $fileName")

                        }
                    }
                    DownloadManager.STATUS_PAUSED -> {
                    }
                    DownloadManager.STATUS_PENDING -> {

                    }
                    DownloadManager.STATUS_RUNNING -> {
                        val total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (total >= 0) {
                            val downloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = (downloaded * 100L / total).toInt()

                        }
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true
                        withContext(Dispatchers.Main) {
                            Log.d("asdasdadad", "Download Completed $fileName")
                            Toast.makeText(getApplication(),
                                "Download Completed $fileName",
                                Toast.LENGTH_SHORT)
                                .show()
                            success.invoke(fileName)
                        }

                    }
                }
            }

            cursor.close()
        }
    }

}