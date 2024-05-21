package com.example.webrtcandroid.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.webrtcandroid.CloseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action?.let { action ->
            if (action == "ACTION_EXIT") {
                context?.let { noneNullContext ->
                    SocketService.stopService(noneNullContext)
                    noneNullContext.startActivity(Intent(noneNullContext, CloseActivity::class.java)
                        .apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                }
            }
        }
    }
}