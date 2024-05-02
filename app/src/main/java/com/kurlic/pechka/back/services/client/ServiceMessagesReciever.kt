package com.kurlic.pechka.back.services.client

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.kurlic.pechka.MainActivity
import com.kurlic.pechka.common.debug.makeToast

class ServiceMessagesReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("vbu", "BroadcastBecome")
        val viewModel = ViewModelProvider(context as MainActivity)[ServiceViewModel::class.java]
        viewModel.loadData(context)
    }
}