package com.eduar.downloadfilespro

interface Downloader {
    fun downloadFile(url: String, fileName: String): Long
}