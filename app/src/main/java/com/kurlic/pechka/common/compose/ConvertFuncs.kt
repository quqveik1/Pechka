package com.kurlic.pechka.common.compose

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

fun Float.toSp(density: Density): TextUnit = with(density) {
    this@toSp.dp.toSp()
}