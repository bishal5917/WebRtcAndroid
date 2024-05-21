package com.example.webrtcandroid.socket

data class MessageModel(
    val from: String, val target: String, val payload: Payload
)

data class Payload(
    val action: String,
    val message: String? = null,
    val connections: List<String>? = null,
    val bePolite: Boolean? = null
)

