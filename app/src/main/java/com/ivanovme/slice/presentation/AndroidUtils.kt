package com.ivanovme.slice.presentation

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.round

fun Int.toPx() = toFloat().toPx().toInt()

fun Float.toPx() =
    round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )
    )

fun screenWidth() = Resources.getSystem().displayMetrics.widthPixels
fun screenHeight() = Resources.getSystem().displayMetrics.heightPixels