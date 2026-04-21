package com.eduar.downloadfilespro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DownloadViewModel : ViewModel() {

    var apiResult by mutableStateOf("Esperando...")
        private set

    var mediaInfoReady by mutableStateOf<MediaResponse?>(null)
        private set

    fun fetchVideoInfo(videoId: String) {
        viewModelScope.launch {
            apiResult = "Consultando al servidor..."
            mediaInfoReady = null

            try {
                val request = VideoRequest(videoId)
                val response = RetrofitClient.apiService.getInfoVideo(request)

                if (response.isSuccessful) {
                    val mediaInfo = response.body()
                    if (mediaInfo != null) {
                        apiResult = "¡Éxito! Canción encontrada."
                        mediaInfoReady = mediaInfo
                    } else {
                        apiResult = "Error: El servidor no devolvió datos."
                    }
                } else {
                    apiResult = "Error del servidor: Código ${response.code()}"
                }
            } catch (e: Exception) {
                apiResult = "Error de conexión: ${e.localizedMessage}"
            }
        }
    }
}