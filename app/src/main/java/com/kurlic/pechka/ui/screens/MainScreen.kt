package com.kurlic.pechka.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.kurlic.pechka.MainActivity
import com.kurlic.pechka.ui.elements.StyledButton

const val MainScreenTag = "MainScreen"

@Composable
fun MainScreen(navController: NavController) {
    val activity = (LocalContext.current as MainActivity)
    val res = activity.permissionManager.checkAllPermissions()
    if(!res) {
        navController.navigate(PermissionsScreenTag) {
            popUpTo(PermissionsScreenTag)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StyledButton(
            text = "Start",
            onClick = { navController.navigate(HeatModeSelectScreenName) })
    }
}