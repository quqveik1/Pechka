package com.kurlic.pechka.back.services.heatservice

import android.content.Context
import com.google.gson.Gson

const val PurposeMessageTag = "purpose"
const val IntentDataMessageTag = "data"

enum class HeatServiceMessageType {
    StopServiceTag,
    StartServiceTag,
    ChangeServiceData
}

const val ForegroundServiceFolder = "HeatForegroundService"
const val ForegroundServiceFolderDataTag = "HeatForegroundServiceData"
const val ForegroundServiceFolderDataMutableFromActivityTag = "HeatForegroundServiceDataMutableFromActivity"

fun <T> saveData(context: Context, folderDataTag: String, data: T) {
    val prefs = context.getSharedPreferences(ForegroundServiceFolder, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    val gson = Gson()
    val json = gson.toJson(data)
    editor.putString(folderDataTag, json)
    editor.apply()
}

inline fun <reified T : Any> loadStoredData(context: Context, folderDataTag: String): T? {
    val prefs = context.getSharedPreferences(ForegroundServiceFolder, Context.MODE_PRIVATE)

    val str = prefs.getString(folderDataTag, null)
    val gson = Gson()
    if (str == null) {
        return null
    }

    return gson.fromJson(str, T::class.java)
}