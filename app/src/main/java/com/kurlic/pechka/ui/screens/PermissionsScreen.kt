package com.kurlic.pechka.ui.screens

import android.content.Context
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.kurlic.pechka.common.debug.makeToast
import com.kurlic.pechka.ui.elements.StyledButton
import com.kurlic.pechka.ui.elements.StyledText

const val PermissionsScreenTag = "PermissionsScreen"

@Composable
@Preview
fun PermissionsScreen(navController: NavController = rememberNavController()) {
    val allPermissionGrantedAfterRequest = remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current as MainActivity
    val scrollState = rememberScrollState()
    val permissionManager = context.permissionManager
    val allPermissionGranted = permissionManager.areAllLifePermissionsGranted.observeAsState()

    if (allPermissionGranted.value == true) {
        leaveScreen(navController)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        StyledText(
            text = "Для работы приложения необходим доступ к уведомлениям.",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        when (allPermissionGrantedAfterRequest.value) {
            true -> {
                leaveScreen(navController)
            }

            false -> {
                StyledText(
                    text = "Вы не предоставили необходимые разрешения. Приложение без этого не будет работать. Сделайте это в ручном режиме или перезапустите приложение.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                StyledButton(text = "Настройки уведомлений",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        openSettings(
                            permissionToActionMap[android.Manifest.permission.POST_NOTIFICATIONS]!!,
                            activity = context
                        )
                    })
            }

            else -> {
                StyledButton(text = "Продолжить",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        context.permissionManager.requestAllPermissions(isGranted = allPermissionGrantedAfterRequest)
                    })
            }
        }
    }
}

fun leaveScreen(navController: NavController) {
    navController.navigate(MainScreenTag) {
        popUpTo(MainScreenTag)
    }
}