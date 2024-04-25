package com.kurlic.pechka.back.services.heatservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson

class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val stopIntent = Intent(context, HeatForegroundService::class.java)
        val gson = Gson()
        val json = gson.toJson(ServiceData(ServiceState.Stopped))
        stopIntent.putExtra(StopIntentTag, json)
        context.startService(stopIntent)
        Log.d(DebugTag, "Service stopped by user.")
    }
}