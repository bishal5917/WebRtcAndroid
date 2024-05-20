package com.example.webrtcandroid.socket

interface SocketEventListener {
    fun onNewMessage(message: MessageModel)
    fun onSocketOpened()
    fun onSocketClosed()
}