package com.kurlic.pechka.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

const val PermissionsScreenTag = "PermissionsScreen"

@Composable
@Preview
fun PermissionsScreen(navController: NavController = rememberNavController()) {
    val con = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(con, "NALIVAI", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(con, "OTKAZ", Toast.LENGTH_SHORT).show()
            }
        }
    )
    Column(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Длинный текст, который может занимать несколько экранов и требовать прокрутки для полного просмотра. " +
                        "Здесь может быть ваше соглашение, информация о разрешениях или другой информационный текст, " +
                        "который необходимо отобразить пользователю. При необходимости текст будет прокручиваться."
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        permissionLauncher.launch(android.Manifest.permission.FOREGROUND_SERVICE)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            ) {
                Text("Продолжить")
            }
        }
    }
}