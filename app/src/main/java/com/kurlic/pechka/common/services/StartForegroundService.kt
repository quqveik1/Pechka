package com.kurlic.pechka.common.services

import android.content.Context
import android.content.Intent
import android.os.Build

fun startForegroundService(context: Context, service: Class<*>) {
    val intent = Intent(context, service)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}

fun launchForegroundService(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}