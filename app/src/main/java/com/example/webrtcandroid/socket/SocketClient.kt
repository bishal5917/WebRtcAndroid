package com.example.webrtcandroid.socket

import com.example.webrtcandroid.utils.GlobalValues
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketClient @Inject constructor(
    private val gson: Gson
) {
    private var socketEventListener: SocketEventListener? = null
    fun setListener(messageInterface: SocketEventListener) {
        this.socketEventListener = messageInterface
    }

    fun onStop() {
        socketEventListener = null
        runCatching { webSocket?.closeBlocking() }
    }

    private var webSocket: WebSocketClient? = null

    init {
        initSocket()
    }

    private fun initSocket() {
        webSocket = object : WebSocketClient(URI(GlobalValues.url)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                socketEventListener?.onSocketOpened()
            }

            override fun onMessage(message: String?) {
                try {
                    socketEventListener?.onNewMessage(
                        gson.fromJson(
                            message, MessageModel::class.java
                        )
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                socketEventListener?.onSocketClosed()

            }

            override fun onError(ex: Exception?) {
            }

        }
        webSocket?.connect()

    }

    fun sendMessageToSocket(messageModel: MessageModel) {
        runCatching {
            webSocket?.send(Gson().toJson(messageModel))
        }
    }

}