package com.kurlic.pechka.back.androidapi

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

val lifePermissions = arrayOf(
    android.Manifest.permission.FOREGROUND_SERVICE,
    android.Manifest.permission.POST_NOTIFICATIONS
)

data class PermissionsState(val isAllPermissionsGranted: Boolean = false)


@Composable
fun requestPermissionSoft(
    activity: ComponentActivity,
    permissionName: String,
    isGrantedState: MutableState<Boolean>
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
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
            isGrantedState.value = isGranted
        }
    )
    permissionLauncher.launch(permissionName)
}

@Composable
fun RequestAllPermissions(isAllPermissionsGranted: MutableState<Boolean>) {
    val context = LocalContext.current
    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(context, "Все разрешения предоставлены", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Некоторые разрешения отклонены", Toast.LENGTH_SHORT).show()
        }
        isAllPermissionsGranted.value = allGranted
    }

    multiplePermissionLauncher.launch(lifePermissions)
}

fun checkPermission(
    context: Context,
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun checkAllPermissions(context: Context): Boolean {
    return lifePermissions.all { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}