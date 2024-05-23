package com.example.webrtcandroid

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webrtcandroid.service.SocketService
import com.example.webrtcandroid.service.WebSocketMessageListener
import com.example.webrtcandroid.service.WebsocketService
import com.example.webrtcandroid.socket.SocketEventSender
import com.example.webrtcandroid.ui.theme.WebRtcAndroidTheme
import com.example.webrtcandroid.utils.GlobalValues
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), WebSocketMessageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WebRtcAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
//                    val permissionLauncher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.RequestMultiplePermissions()
//                    ) {
//                        SocketService.startService(this)
//                    }
//
//                    LaunchedEffect(key1 = Unit) {
//                        permissionLauncher.launch(
//                            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
//                        )
//                    }
                    val client = OkHttpClient()
                    val request: Request = Request.Builder().url(GlobalValues.url).header(
                        "Authorization", "Bearer ${GlobalValues.token}"
                    ).build()
                    val listener = WebsocketService(this)
                    val ws = client.newWebSocket(request, listener)
                }
            }
        }
    }

    override fun onMessageReceived(message: String) {
        Log.d("socket", message)
        runOnUiThread {

        }
    }
}