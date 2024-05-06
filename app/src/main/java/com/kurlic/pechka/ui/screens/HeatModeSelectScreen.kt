package com.kurlic.pechka.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import com.kurlic.pechka.back.services.client.ServiceViewModel
import com.kurlic.pechka.back.services.heatservice.HeatForegroundService
import com.kurlic.pechka.back.services.heatservice.HeatServiceReceiver
import com.kurlic.pechka.back.services.heatservice.ServiceState
import com.kurlic.pechka.back.services.heatservice.StopServiceTag
import com.kurlic.pechka.ui.elements.StyledButton
import com.kurlic.pechka.ui.elements.StyledText
import com.kurlic.pechka.ui.elements.TimeSetter
import com.kurlic.pechka.ui.elements.rememberTimeDataState

const val HeatModeSelectScreenName = "HeatModeSelectScreen"

@Composable @Preview
fun HeatModeSelectScreen(navController: NavController = rememberNavController()) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val serviceViewModel: ServiceViewModel =
            ViewModelProvider(context as MainActivity)[ServiceViewModel::class.java]
        val serviceData = serviceViewModel.serviceData.observeAsState()
        val timeData = rememberTimeDataState(0, 10)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TimeSetter(modifier = Modifier.fillMaxWidth(), timeData)

            AnimatedVisibility(visible = serviceData.value?.state != ServiceState.Active) {
                StyledButton(
                    text = stringResource(id = R.string.start_heating_service) + "!",
                    onClick = {
                        HeatForegroundService.startService(context)
                    })
            }
            AnimatedVisibility(visible = serviceData.value?.state == ServiceState.Active) {
                Column(modifier = Modifier) {
                    StyledText(text = stringResource(id = R.string.service_in_progress))
                    StyledButton(text = stringResource(id = R.string.stop), onClick = {
                        HeatServiceReceiver.sendBroadcastMessage(context, StopServiceTag)
                    }, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}