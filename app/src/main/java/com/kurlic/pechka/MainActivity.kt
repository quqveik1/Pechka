package com.kurlic.pechka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kurlic.pechka.back.androidapi.permission.PermissionManager
import com.kurlic.pechka.back.services.client.ServiceManager
import com.kurlic.pechka.ui.screens.Navigation
import com.kurlic.pechka.ui.theme.PechkaTheme

class MainActivity : ComponentActivity() {
    lateinit var permissionManager: PermissionManager
    lateinit var serviceManager: ServiceManager
    override fun onStart() {
        super.onStart()
        permissionManager = PermissionManager(this)
        serviceManager = ServiceManager(this)
    }

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

    override fun onResume() {
        super.onResume()
        permissionManager.checkAllPermissions()
    }

    override fun onStop() {
        super.onStop()

        serviceManager.onStop()
    }
}