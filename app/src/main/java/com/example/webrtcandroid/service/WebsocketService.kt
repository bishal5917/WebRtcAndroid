package com.example.webrtcandroid.service

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

interface WebSocketMessageListener {
    fun onMessageReceived(message: String)
}

open class WebsocketService(private val messageListener: WebSocketMessageListener) :
    WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("socket", "CONNECTION ESTABLISHED")
        super.onOpen(webSocket, response)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("socket", "CONNECTION CLOSED")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        super.onClosed(webSocket, code, reason)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        messageListener.onMessageReceived(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("socket", "FAILED $response")
        super.onFailure(webSocket, t, response)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("socket", "CLOSING $reason")
        super.onClosing(webSocket, code, reason)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}