package com.kurlic.pechka.common.debug

import android.content.Context
import android.widget.Toast

fun makeToast(
    context: Context,
    text: String,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, text, length).show()
}