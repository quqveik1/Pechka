package com.kurlic.pechka.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kurlic.pechka.MainActivity

const val PermissionsScreenTag = "PermissionsScreen"

@Composable
@Preview
fun PermissionsScreen(navController: NavController = rememberNavController()) {
    val allPermissionGranted = remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Длинный текст, который может занимать несколько экранов и требовать прокрутки для полного просмотра. " + "Здесь может быть ваше соглашение, информация о разрешениях или другой информационный текст, " + "который необходимо отобразить пользователю. При необходимости текст будет прокручиваться."
            )
        }

        if(allPermissionGranted.value == true) {
            Toast.makeText(context, "VBU", Toast.LENGTH_SHORT).show();
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = {
                (context as MainActivity).permissionManager.requestAllPermissions(isGranted = allPermissionGranted)
            }) {
                Text("Продолжить")
            }
        }
    }
}