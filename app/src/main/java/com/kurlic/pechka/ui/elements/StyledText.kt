package com.kurlic.pechka.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.kurlic.pechka.R

@Composable
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = dimensionResource(id = R.dimen.text_size_standard).value.sp
) {
    Text(
        text = text,
        fontSize = fontSize,
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_standard))
    )
}