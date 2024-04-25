package com.kurlic.pechka.back.services

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson

class ServiceViewModel : ViewModel() {
    val serviceData = MutableLiveData<ServiceData>()

    fun loadData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = context.getSharedPreferences(
                ForegroundServiceFolder,
                Context.MODE_PRIVATE
            )

            val str = prefs.getString(
                ForegroundServiceFolderDataTag,
                null
            )
            val gson = Gson()

            val newVal = gson.fromJson(
                str,
                ServiceData::class.java
            )
            serviceData.postValue(newVal)
        }
    }
}