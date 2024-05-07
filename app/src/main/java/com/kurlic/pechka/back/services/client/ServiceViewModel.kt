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
import com.kurlic.pechka.back.services.heatservice.ForegroundServiceFolderDataMutableFromActivityTag
import com.kurlic.pechka.back.services.heatservice.ForegroundServiceFolderDataTag
import com.kurlic.pechka.back.services.heatservice.ServiceData
import com.kurlic.pechka.back.services.heatservice.ServiceDataMutableFromActivity
import com.kurlic.pechka.back.services.heatservice.loadStoredData

class ServiceViewModel : ViewModel() {
    var serviceData = MutableLiveData<ServiceData?>(null)
    var serviceMutableData = MutableLiveData<ServiceDataMutableFromActivity?>(null)

    init {
        Log.d("ViewModel", "ServiceViewModel created")
    }

    fun loadData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val newVal: ServiceData? = loadStoredData(context, ForegroundServiceFolderDataTag)
            serviceData.postValue(newVal)
            val newMutableVal: ServiceDataMutableFromActivity? = loadStoredData(context, ForegroundServiceFolderDataMutableFromActivityTag)
            serviceMutableData.postValue(newMutableVal)
        }
    }
}