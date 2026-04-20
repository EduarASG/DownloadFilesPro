package com.eduar.downloadfilespro

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader(private val context: Context): Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long{
        val request = DownloadManager.Request(url.toUri()).setMimeType("audio/mpeg").setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI).setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE).setTitle("audio/mpeg").addRequestHeader("Authorization", "Bearer <token>").setDestinationInExternalPublicDir(
            Environment.DIRECTORY_MUSIC, "cancion.mpeg")
        return downloadManager.enqueue(request)
    }
}