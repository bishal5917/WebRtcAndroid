package com.example.webrtcandroid.socket

import android.provider.Settings.Global
import android.util.Log
import com.example.webrtcandroid.utils.GlobalValues
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

val SOCKET = "socket"

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
                Log.d(SOCKET, "Connection Success!!!")
                socketEventListener?.onSocketOpened()
            }

            override fun addHeader(token: String?, value: String?) {
                super.addHeader(token, GlobalValues.token)
            }

            override fun onMessage(message: String?) {
                Log.d("SOCKET", "Message received")
                try {
                    val messageModel = gson.fromJson(message, MessageModel::class.java)
                    val action = messageModel.payload.action
                    when (action) {
                        EventTypes.CONNECTION -> handleConnectionEvent(messageModel)
                        EventTypes.DISCONNECT -> handleDisconnectEvent(messageModel)
                        EventTypes.MESSAGE -> handleMessageEvent(messageModel)
                        EventTypes.MESSAGE_ONE -> handleMessageOneEvent(messageModel)
                        EventTypes.READY -> handleReadyEvent(messageModel)
                        else -> socketEventListener?.onNewMessage(messageModel)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            private fun handleConnectionEvent(messageModel: MessageModel) {
                // Handle connection event
                socketEventListener?.onNewMessage(messageModel)
            }

            private fun handleDisconnectEvent(messageModel: MessageModel) {
                // Handle disconnect event
                socketEventListener?.onNewMessage(messageModel)
            }

            private fun handleMessageEvent(messageModel: MessageModel) {
                // Handle message event
                socketEventListener?.onNewMessage(messageModel)
            }

            private fun handleMessageOneEvent(messageModel: MessageModel) {
                // Handle messageOne event
                socketEventListener?.onNewMessage(messageModel)
            }

            private fun handleReadyEvent(messageModel: MessageModel) {
                // Handle ready event
                socketEventListener?.onNewMessage(messageModel)
                sendReadyEvent()
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(SOCKET, "CLOSED:$reason")
                socketEventListener?.onSocketClosed()

            }

            private fun sendReadyEvent() {
                val readyEvent = mapOf(
                    "from" to "peerId", // Replace with actual peerId
                    "target" to "all", "payload" to mapOf(
                        "action" to "ready"
                    )
                )
                sendMessageToSocket(gson.toJson(readyEvent))
            }

            override fun onError(ex: Exception?) {
                Log.d(SOCKET, ex?.message.toString())
            }

        }
        webSocket?.connect()

    }

    fun sendMessageToSocket(message: String) {
        runCatching {
            webSocket?.send(message)
        }
    }

}