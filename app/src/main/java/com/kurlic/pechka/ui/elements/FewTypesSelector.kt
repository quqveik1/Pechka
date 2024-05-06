package com.kurlic.pechka.ui.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kurlic.pechka.common.debug.makeToast

@Composable @Preview fun FewTypesSelector(
    modifier: Modifier = Modifier, objectsList: List<String> = listOf("Piki", "Eshak", "Krok")
) {
    val context = LocalContext.current
    val selectedItemIndex = rememberSaveable {
        mutableIntStateOf(objectsList.size / 2)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        for (i in objectsList.indices) {
            val alpha: Float by animateFloatAsState(if (selectedItemIndex.intValue == i) 1f else 0f)
            val selectedColor = MaterialTheme.colorScheme.primary
            Box(
                modifier = Modifier
                    .background(selectedColor.copy(alpha))
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                StyledText(text = objectsList[i], modifier = Modifier.clickable(indication = null,
                                                                                interactionSource = remember { MutableInteractionSource() }) {
                    selectedItemIndex.intValue = i
                })
            }
        }
    }
}