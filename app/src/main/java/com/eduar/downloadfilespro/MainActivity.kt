package com.eduar.downloadfilespro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// Paleta de colores Vaporwave
val VaporDark = Color(0xFF140C1A)
val VaporPink = Color(0xFFFF00FF)
val VaporCyan = Color(0xFF00FFFF)
val VaporCardBg = Color(0xFF1F1529)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val downloader = AndroidDownloader(this)
        setContent {
            VaporwaveTheme {
                DownloadScreen(downloader)
            }
        }
    }
}

@Composable
fun VaporwaveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = VaporDark,
            surface = VaporDark
        )
    ) {
        Surface(color = VaporDark, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun DownloadScreen(downloader: AndroidDownloader, viewModel: DownloadViewModel = viewModel()) {
    var url by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = VaporDark,
        topBar = { VaporTopBar() },
        bottomBar = { VaporBottomBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            RetroCard(borderColor = VaporCyan, title = "TARGET VECTOR") {
                Text("URL / URI", color = VaporCyan, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))

                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = { Text("https://...", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VaporPink,
                        unfocusedBorderColor = VaporPink,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        extractYoutubeId(url)?.let { id ->
                            viewModel.fetchVideoInfo(id)
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = VaporPink, contentColor = Color.Black)
                ) {
                    Text("CONSULT VIDEO", fontWeight = FontWeight.Bold)
                }

                if (viewModel.apiResult.isNotEmpty()) {
                    Text(text = viewModel.apiResult, color = VaporCyan, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            viewModel.mediaInfoReady?.let { info ->
                RetroCard(borderColor = VaporPink, title = "MEDIA_PAYLOAD.OBJ", isWindowStyle = true) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color.Black)
                            .border(1.dp, VaporPink, RectangleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("PREVIEW", color = VaporPink)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = info.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "Format: MP3/Audio", color = Color.LightGray, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { downloader.downloadFile(info.url, info.name) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = VaporCyan, contentColor = Color.Black)
                    ) {
                        Text("DOWNLOAD NOW", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RetroCard(borderColor: Color, title: String, isWindowStyle: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RectangleShape)
            .background(VaporCardBg)
    ) {
        if (isWindowStyle) {
            Row(
                modifier = Modifier.fillMaxWidth().background(borderColor).padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(10.dp).border(1.dp, Color.Black).background(Color.Transparent))
                    Box(modifier = Modifier.size(10.dp).border(1.dp, Color.Black).background(Color.Transparent))
                    Box(modifier = Modifier.size(10.dp).background(Color.Black))
                }
            }
        } else {
            Box(modifier = Modifier.padding(start = 12.dp).offset(y = (-10).dp).background(VaporDark)) {
                Text(title, color = borderColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp), fontSize = 12.sp)
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun VaporTopBar() {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth().height(64.dp).background(VaporDark),
            contentAlignment = Alignment.Center
        ) {
            Text("VAPOR_DRIVE V1.0", color = VaporPink, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 18.sp)
        }
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(VaporPink))
    }
}

@Composable
fun VaporBottomBar() {
    Column {
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(VaporPink))
        NavigationBar(containerColor = Color.Black) {
            NavigationBarItem(
                selected = true,
                onClick = { },
                icon = { Icon(Icons.Default.Search, contentDescription = null) },
                label = { Text("SEARCH") },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, selectedTextColor = VaporPink, indicatorColor = VaporPink, unselectedIconColor = VaporPink, unselectedTextColor = VaporPink)
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.SyncAlt, contentDescription = null) },
                label = { Text("TRANSFERS") },
                colors = NavigationBarItemDefaults.colors(unselectedIconColor = VaporPink, unselectedTextColor = VaporPink)
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.Archive, contentDescription = null) },
                label = { Text("ARCHIVE") },
                colors = NavigationBarItemDefaults.colors(unselectedIconColor = VaporPink, unselectedTextColor = VaporPink)
            )
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