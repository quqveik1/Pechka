package com.kurlic.pechka.back.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServiceViewModel : ViewModel() {
    val serviceData = MutableLiveData<ServiceData>()
}