package com.kurlic.pechka.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kurlic.pechka.ui.elements.TimeSetter

const val TestScreenTag = "TestScreenTag"

@Composable @Preview fun TestScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        TimeSetter()
    }
}