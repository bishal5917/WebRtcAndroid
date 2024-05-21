package com.example.webrtcandroid.socket

import com.example.webrtcandroid.utils.GlobalValues
import javax.inject.Inject

class SocketEventSender @Inject constructor(
    private val socketClient: SocketClient
) {
    fun sendMessage(message: String) {
//        socketClient.sendMessageToSocket(
//            MessageModel(
//                from = GlobalValues.username,
//                target = "all",
//                payload = Payload(message = message, action = EventTypes.MESSAGE)
//            )
//        )
    }
}