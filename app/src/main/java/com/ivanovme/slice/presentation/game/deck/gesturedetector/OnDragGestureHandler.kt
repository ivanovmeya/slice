package com.ivanovme.slice.presentation.game.deck.gesturedetector

import android.view.MotionEvent

interface OnDragGestureHandler {
    fun onDrag(
        origin: MotionEvent,
        current: MotionEvent
    )

    fun onFling(
        origin: MotionEvent,
        current: MotionEvent,
        velocityX: Float
    )

    fun onDragFinished(origin: MotionEvent, last: MotionEvent)
}