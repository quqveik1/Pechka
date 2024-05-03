package com.kurlic.pechka.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kurlic.pechka.MainActivity
import com.kurlic.pechka.R
import com.kurlic.pechka.back.androidapi.permission.PermissionManager
import com.kurlic.pechka.back.androidapi.permission.PermissionViewModel
import com.kurlic.pechka.back.androidapi.permission.openSettings
import com.kurlic.pechka.back.androidapi.permission.permissionToActionMap
import com.kurlic.pechka.ui.elements.StyledButton
import com.kurlic.pechka.ui.elements.StyledDivider
import com.kurlic.pechka.ui.elements.StyledText

const val PermissionsScreenTag = "PermissionsScreen"

@Composable @Preview fun PermissionsScreen(navController: NavController = rememberNavController()) {
    val allPermissionGrantedAfterRequest = remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current as MainActivity
    val scrollState = rememberScrollState()
    val permissionViewModel = ViewModelProvider(context)[PermissionViewModel::class.java]
    val allPermissionGranted = permissionViewModel.areAllLifePermissionsGranted.observeAsState()

    if (allPermissionGranted.value == true) {
        leaveScreen(navController)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        StyledText(
            text = stringResource(id = R.string.permissions_soft_request),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        when (allPermissionGrantedAfterRequest.value) {
            true -> {
                leaveScreen(navController)
            }

            false -> {
                StyledDivider()
                StyledText(
                    text = stringResource(id = R.string.user_declines_permission),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                StyledDivider()
                StyledButton(text = stringResource(R.string.notification_settings),
                             modifier = Modifier.align(Alignment.CenterHorizontally),
                             onClick = {
                                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                     openSettings(
                                         permissionToActionMap[Manifest.permission.POST_NOTIFICATIONS]!!,
                                         activity = context
                                     )
                                 }
                             })
            }

            else -> {
                StyledDivider()
                StyledButton(text = stringResource(R.string.continue_app),
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