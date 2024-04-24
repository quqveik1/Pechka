package com.kurlic.pechka.back.androidapi.permission

import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity

fun openSettings(
    stringAction: String,
    activity: ComponentActivity
) {
    val intent = Intent().apply {
        action = stringAction

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            putExtra("android.provider.extra.APP_PACKAGE", activity.packageName)
        } else {
            putExtra("app_package", activity.packageName)
            putExtra("app_uid", activity.applicationInfo.uid)
        }
    }
    activity.startActivity(intent)
}