package com.kurlic.pechka.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.kurlic.pechka.back.androidapi.openSettings
import com.kurlic.pechka.back.androidapi.permissionToActionMap
import com.kurlic.pechka.ui.elements.StyledButton
import com.kurlic.pechka.ui.elements.StyledText

const val PermissionsScreenTag = "PermissionsScreen"

@Composable
@Preview
fun PermissionsScreen(navController: NavController = rememberNavController()) {
    val allPermissionGranted = remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        StyledText(
            text = "Длинный текст, который может занимать несколько экранов и требовать прокрутки для полного просмотра. " + "Здесь может быть ваше соглашение, информация о разрешениях или другой информационный текст, " + "который необходимо отобразить пользователю. При необходимости текст будет прокручиваться."
        )

        if (allPermissionGranted.value == true) {
            Toast.makeText(
                context,
                "VBU",
                Toast.LENGTH_SHORT
            ).show();
            navController.navigate(MainScreenTag)
        } else if (allPermissionGranted.value == false) {
            StyledText(text = "Вы не предоставили необходимые разрешения. Приложение без этого не будет работать. Сделайте это в ручном режиме или перезапустите приложение.")
            StyledButton(
                text = "Настройки уведомлений",
                onClick = {
                    openSettings(
                        permissionToActionMap[android.Manifest.permission.POST_NOTIFICATIONS]!!,
                        activity = (context as MainActivity)
                    )
                })
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                StyledButton(text = "Продолжить",
                    onClick = {
                        (context as MainActivity).permissionManager.requestAllPermissions(isGranted = allPermissionGranted)
                    })
            }
        }
    }
}