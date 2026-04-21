package com.eduar.downloadfilespro

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.net.toUri

class AndroidDownloader(private val context: Context): Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String, fileName: String): Long {
        val uri = Uri.parse(url)

        // Extrae la extensión de la URL (ej: mp4, m4a) o usa mp3 por defecto
        val extension = MimeTypeMap.getFileExtensionFromUrl(url).ifEmpty { "mp3" }

        // Evita duplicar la extensión si el nombre ya la trae
        val finalFileName = if (fileName.contains(".")) fileName else "$fileName.$extension"

        val request = DownloadManager.Request(uri)
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(finalFileName)
            .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_MUSIC, finalFileName)

        return downloadManager.enqueue(request)
    }
}