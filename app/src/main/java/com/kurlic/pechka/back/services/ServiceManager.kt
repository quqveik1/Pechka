package com.kurlic.pechka.back.services

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import com.kurlic.pechka.MainActivity

class ServiceManager(val activity: MainActivity) {

    val serviceMessagesReceiver = ServiceMessagesReceiver()

    init {
        val filter = IntentFilter(ForegroundServiceBroadCast)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(
                serviceMessagesReceiver,
                filter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            activity.registerReceiver(
                serviceMessagesReceiver,
                filter
            )
        }
    }

    fun onStop() {
        activity.unregisterReceiver(serviceMessagesReceiver)
    }
}