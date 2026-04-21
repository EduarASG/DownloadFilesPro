package com.eduar.downloadfilespro


import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val downloader = AndroidDownloader(this)
        setContent {
            DownloadScreen(downloader)
        }
    }
}

@Composable
fun DownloadScreen(downloader: AndroidDownloader, viewModel: DownloadViewModel = viewModel()) {
    var url by remember { mutableStateOf("") }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL de YouTube") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    extractYoutubeId(url)?.let { id ->
                        viewModel.fetchVideoInfo(id)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Consultar Video")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = viewModel.apiResult)

            viewModel.mediaInfoReady?.let { info ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Archivo: ${info.name}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { downloader.downloadFile(info.url, info.name) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Descargar ahora")
                }
            }
        }
    }
}

fun extractYoutubeId(url: String): String? {
    return try {
        val uri = Uri.parse(url)
        var videoId = uri.getQueryParameter("v")
        if (videoId == null && uri.host?.contains("youtu.be") == true) {
            videoId = uri.lastPathSegment
        }
        videoId
    } catch (e: Exception) {
        null
    }
}