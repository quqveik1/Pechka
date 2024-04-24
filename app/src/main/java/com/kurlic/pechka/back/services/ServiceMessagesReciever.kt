package com.kurlic.pechka.back.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kurlic.pechka.common.debug.makeToast

class ServiceMessagesReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("vbu", "BroadcastBecome")
        makeToast(context, "ServiceMessagesReceiver")
    }
}