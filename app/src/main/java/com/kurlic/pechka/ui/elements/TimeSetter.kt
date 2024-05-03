package com.kurlic.pechka.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kurlic.pechka.common.debug.makeToast
import kotlinx.coroutines.launch


@Composable @Preview fun TimeSetter(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .height(200.dp)
) {

    ConstraintLayout(modifier = modifier) {
        val (left, mid, right) = createRefs()
        CyclicLazyTimeList(modifier = Modifier.constrainAs(left) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(mid.start)
        }, rangeStart = 0, rangeEnd = 24)
        Box(modifier = Modifier.constrainAs(mid) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(left.end)
            end.linkTo(right.start)
        }, contentAlignment = Alignment.Center) {
            StyledText(text = ":", fontSize = 80.sp)
        }
        CyclicLazyTimeList(
            modifier = Modifier.constrainAs(right) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(mid.end)
                end.linkTo(parent.end)
            }, rangeStart = 0, rangeEnd = 60, startDelta = 10
        )
    }

}

@Composable fun CyclicLazyTimeList(
    modifier: Modifier = Modifier, rangeStart: Int, rangeEnd: Int, startDelta: Int = 0
) {
    val listRealLen = rangeEnd - rangeStart + 1
    val listLen: Int = Short.MAX_VALUE.toInt()
    val startLen: Int =
        (Short.MAX_VALUE.toInt() / 2 - (Short.MAX_VALUE.toInt() / 2) % listRealLen) + startDelta
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startLen)

    if (listRealLen <= 0) {
        throw NegativeArraySizeException()
    }

    LazyColumn(
        state = listState, modifier = modifier
    ) {
        items(listLen) { index ->
            val time = (index % listRealLen)

            Text(modifier = Modifier, text = time.toString(), fontSize = 80.sp)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val wasScrolled = remember { mutableStateOf(false) }
    val context = LocalContext.current
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