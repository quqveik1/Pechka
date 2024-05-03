package com.kurlic.pechka.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kurlic.pechka.ui.elements.TimeSetter

const val TestScreenTag = "TestScreenTag"

@Composable @Preview fun TestScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        TimeSetter(modifier = Modifier.fillMaxWidth().height(300.dp))
    }
}