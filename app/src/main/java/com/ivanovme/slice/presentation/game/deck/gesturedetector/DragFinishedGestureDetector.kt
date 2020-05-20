package com.ivanovme.slice.presentation.game.deck.gesturedetector


import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat

/**
Extended gesture detector. Tracks onScroll, onFling, and onDragFinished gesture events
 */
class DragFinishedGestureDetector(
    context: Context,
    private val dragHandler: OnDragGestureHandler
) : View.OnTouchListener {

    private var isDragging = false

    private var dragStartEvent: MotionEvent? = null

    private val dragDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean = true

            override fun onScroll(
                origin: MotionEvent,
                current: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {

                if (!isDragging) {
                    isDragging = true
                    dragStartEvent = origin
                }

                dragHandler.onDrag(origin, current)
                return true
            }

            override fun onFling(
                origin: MotionEvent,
                current: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                isDragging = false
                dragHandler.onFling((dragStartEvent ?: origin), current, velocityX)
                return true
            }
        })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(t: View, event: MotionEvent): Boolean {
        dragDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_UP ->
                if (isDragging) {
                    dragHandler.onDragFinished((dragStartEvent ?: event), event)
                    isDragging = false
                }
        }
        return true
    }

    fun onIntercept(event: MotionEvent): Boolean = dragDetector.onTouchEvent(event)

}