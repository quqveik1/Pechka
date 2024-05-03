package com.kurlic.pechka.ui.elements

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.Pager
import com.kurlic.pechka.common.debug.makeToast
import kotlinx.coroutines.launch


@Composable @Preview fun TimeSetter(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .height(500.dp)
) {
    val timeList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    val listLen: Int = Short.MAX_VALUE.toInt()
    val startLen: Int = Short.MAX_VALUE.toInt() / 2 - 3
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startLen)

    val context = LocalContext.current

    Box(
        modifier = modifier
    ) {
        Row {
            LazyColumn(
                state = listState, modifier = Modifier.weight(1f)
            ) {
                items(listLen) { index ->
                    val time = timeList[index % timeList.size]
                    Text(text = time.toString(), fontSize = 80.sp)
                }
            }
        }

    }

    val coroutineScope = rememberCoroutineScope()
    val wasScrolled = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && wasScrolled.value) {
            coroutineScope.launch {
                listState.animateScrollToItem(listState.firstVisibleItemIndex)
            }
            makeToast(context, "MAMA")
        }

        wasScrolled.value = listState.isScrollInProgress
    }
}