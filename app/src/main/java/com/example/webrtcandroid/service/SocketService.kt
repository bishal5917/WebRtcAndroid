package com.example.webrtcandroid.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.webrtcandroid.MainActivity
import com.example.webrtcandroid.R
import com.example.webrtcandroid.socket.MessageModel
import com.example.webrtcandroid.socket.SocketClient
import com.example.webrtcandroid.socket.SocketEventListener
import com.example.webrtcandroid.socket.SocketEventSender
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SocketService : Service(), SocketEventListener {

    //injecting dependencies
    @Inject
    lateinit var socketClient: SocketClient

    @Inject
    lateinit var eventSender: SocketEventSender

    private lateinit var mainNotification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(
            NotificationManager::class.java
        )
        createNotifications()
        socketClient.setListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when (it?.action) {
                ServiceActions.START.name -> handleStartService()
                ServiceActions.STOP.name -> handleStopService()
                else -> Unit
            }
        }
        return START_STICKY
    }

    private fun handleStartService() {
        if (!isServiceRunning) {
            isServiceRunning = true;
            startForeground(MAIN_NOTIFICATION_ID, mainNotification.build())
            eventSender.sendMessage("HELLO FROM BSAL")
        }
    }

    private fun handleStopService() {
        if (isServiceRunning) {
            isServiceRunning = false
            socketClient.onStop()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    //binders
    private val binder: LocalBinder = LocalBinder()

    inner class LocalBinder() : Binder() {
        fun getService(): SocketService = this@SocketService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    private fun createNotifications() {
        val callChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(callChannel)
        val contentIntent = Intent(
            this, MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), contentIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificationChannel = NotificationChannel(
            "chanel_terminal_bluetooth",
            "chanel_terminal_bluetooth",
            NotificationManager.IMPORTANCE_HIGH
        )

        val intent = Intent(this, CallBroadcastReceiver::class.java).apply {
            action = "ACTION_EXIT"
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        notificationManager.createNotificationChannel(notificationChannel)
        mainNotification = NotificationCompat.Builder(
            this, "chanel_terminal_bluetooth"
        ).setSmallIcon(R.mipmap.ic_launcher).setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setOnlyAlertOnce(false)
            .addAction(R.mipmap.ic_launcher, "Exit", pendingIntent)
            .setContentIntent(contentPendingIntent)
    }

    //Overriden functions
    // Override SocketEventListener methods
    override fun onNewMessage(message: MessageModel) {
        when (message.payload.action) {
            "open" -> handleReadyEvent(message)
            else -> {
                // Handle other events
            }
        }
    }

    private fun handleReadyEvent(message: MessageModel) {
        Log.d("SOCKET", "connected connected connected")
        // Update state or notify UI about the new connections
    }

    override fun onSocketOpened() {
    }

    override fun onSocketClosed() {
    }

    companion object {
        var isServiceRunning = false
        const val NOTIFICATION_CHANNEL_ID = "MESSAGE_CHANNEL"
        const val MAIN_NOTIFICATION_ID = 1234

        fun startService(context: Context) {
            Thread {
                startIntent(context, Intent(context, SocketService::class.java).apply {
                    action = ServiceActions.START.name
                })
            }.start()
        }

        fun stopService(context: Context) {
            startIntent(context, Intent(context, SocketService::class.java).apply {
                action = ServiceActions.STOP.name
            })
        }

        private fun startIntent(context: Context, intent: Intent) {
            context.startForegroundService(intent)
        }

    }
}
