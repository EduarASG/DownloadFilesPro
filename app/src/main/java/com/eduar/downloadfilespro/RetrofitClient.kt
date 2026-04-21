package com.eduar.downloadfilespro

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://downloaderapi.franciscorojas.dev/"

    // Creamos un cliente que espera hasta 60 segundos en lugar de 10
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // Tiempo para conectar
        .readTimeout(60, TimeUnit.SECONDS)    // Tiempo para recibir respuesta
        .writeTimeout(60, TimeUnit.SECONDS)   // Tiempo para enviar datos
        .build()

    val apiService: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <- Le pasamos el cliente modificado aquí
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}