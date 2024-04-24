package com.kurlic.pechka.ui.screens

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kurlic.pechka.R
import com.kurlic.pechka.back.services.HeatForegroundService
import com.kurlic.pechka.ui.elements.StyledButton

const val HeatModeSelectScreenName = "HeatModeSelectScreen"

@Composable
@Preview
fun HeatModeSelectScreen(navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StyledButton(
            text = stringResource(id = R.string.start_heating_service),
            onClick = {
                val intent = Intent(context, HeatForegroundService::class.java)
                if(Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            })
    }
}