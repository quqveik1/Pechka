package com.kurlic.pechka.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kurlic.pechka.ui.elements.StyledText
import com.kurlic.pechka.ui.elements.TimeSetter
import com.kurlic.pechka.ui.elements.rememberTimeDataState

const val TestScreenTag = "TestScreenTag"

@Composable @Preview fun TestScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        val timeSetData = rememberTimeDataState(0, 10)

        Column {
            TimeSetter(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                timeData = timeSetData
            )
            Row {
                StyledText(text = timeSetData.mutableHours.value.toString())
                StyledText(text = timeSetData.mutableMinutes.value.toString())
            }

        }
    }
}