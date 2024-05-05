package com.kurlic.pechka.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch

@Stable class TimeData(defHours: Int = 0, defMinutes: Int = 0) {
    val mutableHours = mutableStateOf(defHours)
    val mutableMinutes = mutableStateOf(defMinutes)

    fun getTimeInSeconds(): Int {
        return mutableHours.value * 60 + mutableMinutes.value
    }
}

@Composable fun rememberTimeDataState(defHours: Int = 0, defMinutes: Int = 0): TimeData {
    return remember {
        TimeData(defHours, defMinutes)
    }
}

@Composable @Preview fun TimeSetter(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .height(200.dp), timeData: TimeData = TimeData()
) {
    Row(modifier = modifier) {
        CyclicLazyTimeList(
            modifier = Modifier.weight(1f),
            rangeStart = 0,
            rangeEnd = 24,
            selectedTime = timeData.mutableHours
        )
        ConstraintLayout(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            val mObj = createRef()
            StyledText(modifier = Modifier.constrainAs(mObj) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, text = ":", fontSize = 80.sp)
        }
        CyclicLazyTimeList(
            modifier = Modifier.weight(1f),
            rangeStart = 0,
            rangeEnd = 60,
            selectedTime = timeData.mutableMinutes
        )
    }
}

@Composable fun CyclicLazyTimeList(
    modifier: Modifier = Modifier, rangeStart: Int, rangeEnd: Int, selectedTime: MutableState<Int>
) {
    val listRealLen = rangeEnd - rangeStart + 1
    val listLen: Int = Short.MAX_VALUE.toInt()
    val startLen: Int =
        (Short.MAX_VALUE.toInt() / 2 - (Short.MAX_VALUE.toInt() / 2) % listRealLen) + selectedTime.value
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startLen)

    val getNumberFromIndex = { index: Int ->
        index % listRealLen
    }


    if (listRealLen <= 0) {
        throw NegativeArraySizeException()
    }

    LazyColumn(
        state = listState, modifier = modifier
    ) {
        items(listLen) { index ->
            val time = getNumberFromIndex(index)

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(modifier = Modifier, text = time.toString(), fontSize = 80.sp)
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val wasScrolled = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(key1 = listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && wasScrolled.value) {
            coroutineScope.launch {
                listState.animateScrollToItem(listState.firstVisibleItemIndex)
            }
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        wasScrolled.value = listState.isScrollInProgress
    }

    LaunchedEffect(key1 = listState.firstVisibleItemIndex) {
        selectedTime.value = getNumberFromIndex(listState.firstVisibleItemIndex + 1)
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

}