package com.kurlic.pechka.back.androidapi

val lifePermissions = arrayOf(
    android.Manifest.permission.POST_NOTIFICATIONS
)

val permissionToActionMap = mapOf(
    android.Manifest.permission.POST_NOTIFICATIONS to "android.settings.APP_NOTIFICATION_SETTINGS"
)