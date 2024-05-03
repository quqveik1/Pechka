package com.kurlic.pechka.back.androidapi.permission

import android.os.Build

val lifePermissions: Array<String> by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        arrayOf()
    }
}

val permissionToActionMap: Map<String, String> by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mapOf(
            android.Manifest.permission.POST_NOTIFICATIONS to "android.settings.APP_NOTIFICATION_SETTINGS"
        )
    } else {
        mapOf()
    }
}