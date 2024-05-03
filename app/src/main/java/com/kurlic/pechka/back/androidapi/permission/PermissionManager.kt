package com.kurlic.pechka.back.androidapi.permission

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCompositionContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PermissionManager(private val activity: ComponentActivity) {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var multiplePermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var isGrantedAfterRequest: MutableState<Boolean?>
    private var permissionViewModel = ViewModelProvider(activity)[PermissionViewModel::class.java]

    init {
        setupPermissionRequest()
    }

    private fun setupPermissionRequest() {
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(activity, "NALIVAI", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "OTKAZ", Toast.LENGTH_SHORT).show()
            }
            this.isGrantedAfterRequest.value = isGranted
            permissionViewModel.areAllLifePermissionsGranted.value = isGranted
        }

        multiplePermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            this.isGrantedAfterRequest.value = allGranted
            permissionViewModel.areAllLifePermissionsGranted.value = allGranted
        }
    }

    private fun requestPermission(permissionName: String, isGranted: MutableState<Boolean?>) {
        this.isGrantedAfterRequest = isGranted
        permissionLauncher.launch(permissionName)
    }

    private fun requestMultiplePermissions(
        permissions: Array<String>, isGranted: MutableState<Boolean?>
    ) {
        this.isGrantedAfterRequest = isGranted
        multiplePermissionLauncher.launch(permissions)
    }

    fun requestAllPermissions(isGranted: MutableState<Boolean?>) {
        requestMultiplePermissions(lifePermissions, isGranted)
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkAllPermissions(permissions: Array<String>): Boolean {
        return permissions.all { checkPermission(it) }
    }

    fun checkAllPermissions(): Boolean {
        val res = checkAllPermissions(lifePermissions)
        permissionViewModel.areAllLifePermissionsGranted.value = res
        return res
    }
}