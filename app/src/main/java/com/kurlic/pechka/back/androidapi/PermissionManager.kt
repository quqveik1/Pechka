package com.kurlic.pechka.back.androidapi

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat

class PermissionManager(val activity: ComponentActivity) {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var multiplePermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var isGranted: MutableState<Boolean?>

    init {
        setupPermissionRequest()
    }

    fun setupPermissionRequest() {
        permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Toast.makeText(
                        activity,
                        "NALIVAI",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "OTKAZ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.isGranted.value = isGranted
            }

        multiplePermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allGranted = permissions.entries.all { it.value }
                if (allGranted) {
                    Toast.makeText(
                        activity,
                        "Все разрешения предоставлены",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Некоторые разрешения отклонены",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.isGranted.value = allGranted
            }
    }

    private fun requestPermission(
        permissionName: String,
        isGranted: MutableState<Boolean?>
    ) {
        this.isGranted = isGranted
        permissionLauncher.launch(permissionName)
    }

    private fun requestMultiplePermissions(
        permissions: Array<String>,
        isGranted: MutableState<Boolean?>
    ) {
        this.isGranted = isGranted
        multiplePermissionLauncher.launch(permissions)
    }

    fun requestAllPermissions(isGranted: MutableState<Boolean?>) {
        requestMultiplePermissions(
            lifePermissions,
            isGranted
        )
    }

    fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkAllPermissions(permissions: Array<String>): Boolean {
        return permissions.all { checkPermission(it) }
    }

    fun checkAllPermissions(): Boolean {
        return checkAllPermissions(lifePermissions)
    }
}