package com.kurlic.pechka.back.services.client

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.kurlic.pechka.back.services.heatservice.ForegroundServiceFolder
import com.kurlic.pechka.back.services.heatservice.ForegroundServiceFolderDataTag
import com.kurlic.pechka.back.services.heatservice.ServiceData

class ServiceViewModel : ViewModel() {
    var serviceData = MutableLiveData<ServiceData?>(null)
    init {
        Log.d("ViewModel", "ServiceViewModel created")
    }

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