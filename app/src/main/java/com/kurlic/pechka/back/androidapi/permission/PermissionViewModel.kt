package com.kurlic.pechka.back.androidapi.permission

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {
    val areAllLifePermissionsGranted = MutableLiveData<Boolean?>(null)
}