package com.kurlic.pechka

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.kurlic.pechka.ui.screens.Navigation
import com.kurlic.pechka.ui.theme.PechkaTheme

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var multiplePermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PechkaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }

    private fun setupPermissionRequest() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "NALIVAI", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "OTKAZ", Toast.LENGTH_SHORT).show()
            }
        }

        multiplePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                Toast.makeText(this, "Все разрешения предоставлены", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Некоторые разрешения отклонены", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission(permissionName: String) {
        permissionLauncher.launch(permissionName)
    }

    private fun requestMultiplePermissions(permissions: Array<String>) {
        multiplePermissionLauncher.launch(permissions)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkAllPermissions(permissions: Array<String>): Boolean {
        return permissions.all { checkPermission(it) }
    }
}