package com.eduar.downloadfilespro

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("mp3/info")
    suspend fun getInfoVideo(@Body post: VideoRequest): Response<MediaResponse>
}