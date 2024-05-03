package com.kurlic.pechka.back.services.client

import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import com.kurlic.pechka.MainActivity
import com.kurlic.pechka.back.services.heatservice.ForegroundServiceBroadCast

class ServiceManager(private val activity: MainActivity) {

    private val serviceMessagesReceiver = ServiceMessagesReceiver()
    private val serviceViewModel = ViewModelProvider(activity)[ServiceViewModel::class.java]

    init {
        serviceViewModel.loadData(activity)
        val filter = IntentFilter(ForegroundServiceBroadCast)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.registerReceiver(serviceMessagesReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            activity.registerReceiver(serviceMessagesReceiver, filter)
        }
    }

    fun onStop() {
        activity.unregisterReceiver(serviceMessagesReceiver)
    }
}