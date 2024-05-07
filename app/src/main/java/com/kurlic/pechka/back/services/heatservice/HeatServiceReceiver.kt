package com.kurlic.pechka.back.services.heatservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson

class HeatServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val tag = intent.getStringExtra(PurposeMessageTag)
        if (tag == HeatServiceMessageType.StopServiceTag.name) {
            val stopIntent = Intent(context, HeatForegroundService::class.java)
            val gson = Gson()
            val json = gson.toJson(ServiceData(ServiceState.Stopped))
            stopIntent.putExtra(StopIntentTag, json)
            context.startService(stopIntent)
            Log.d(DebugTag, "Service stopped by user.")
        }
    }

    companion object {
        fun getBroadcastIntent(context: Context, purposeString: String): Intent {
            val intent = Intent(context, HeatServiceReceiver::class.java)
            intent.putExtra(PurposeMessageTag, purposeString)
            return intent
        }

        fun sendBroadcastMessage(context: Context, purposeString: String) {
            context.sendBroadcast(getBroadcastIntent(context, purposeString))
        }
    }
}