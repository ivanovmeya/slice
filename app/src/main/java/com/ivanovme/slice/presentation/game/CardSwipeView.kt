package com.ivanovme.slice.presentation.game

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.GravityCompat
import com.ivanovme.slice.presentation.toPx
import java.util.*

/**
 * Deck of cards with some data provider (like adapter in recycler view)
 * support horizontal swipe and drag gestures.
 * provide callback for drag gesture (to update scale of answers buttons)
 * provide callback gesture finished (success drag/swipe to left or right)
 * intercepts touch event
 *
 *
 *
 * deck's top card scale and rotation on swipe or drag gesture
 * deck's second card scale on swipe or drag gesture
 */
//TODO
//What to do first?
//gesture detector or data provider aka adapter ?
//let's go with detector
class CardSwipeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            origin: MotionEvent, //The first down motion event that started the fling.
            current: MotionEvent, //The move motion event that triggered the current onFling.
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            println("onFling : e1 = ${origin.x}, ${origin.rawX}l; e2 = ${current.y}, ${current.rawY};  velocityX = $velocityX, velocityY = $velocityY")
            return true
        }

        override fun onScroll(
            origin: MotionEvent, //The first down motion event that started the scrolling.
            current: MotionEvent, //The move motion event that triggered the current onScroll.
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            println("onScroll : e1 = x=${origin.x}, rawX=${origin.rawX}; y=${origin.y}, rawY=${origin.rawY};  e2 = x=${current.x}, rawX=${current.rawX}; y=${current.y}, rawY=${current.rawY}; velocityX = $distanceX, velocityY = $distanceY")

            //take top card on the deck and animate scroll
            val topCard = cardsDeck.element()
            topCard.rotation = current.rawX % 90
            topCard.translationX = origin.rawX - current.rawX
            return true
        }
    }

    private val gestureDetector = GestureDetectorCompat(context, gestureListener)

    private val cardsDeck: Queue<View> = LinkedList()

    init {

        val topCardView = FrameLayout(context).apply {
            setBackgroundColor(Color.MAGENTA)
            layoutParams = LayoutParams(300.toPx(), 300.toPx()).apply {
                gravity = Gravity.CENTER
            }
        }

        val behindCardView = FrameLayout(context).apply {
            setBackgroundColor(Color.GREEN)
            layoutParams = LayoutParams(300.toPx(), 300.toPx()).apply {
                gravity = Gravity.CENTER
            }
        }
        addView(behindCardView)
        addView(topCardView)

        cardsDeck.offer(topCardView)
        cardsDeck.offer(behindCardView)

        //TODO implement views attach and detach on fling gesture, for reusing them
    }

    fun onGestureDetected() {

    }

    fun addDragChangeListener() {

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //TODO rewrite with ||
        //TODO OR SHOULD we intercept with touch slop and distance in raw OnTouch with action = MotionEvent.ACTION_MOVE ?
        //TODO REWRITE
        if (gestureDetector.onTouchEvent(ev)) {
            println("onInterceptTouchEvent")
            return true
        }
        return super.onInterceptTouchEvent(ev)
    }

    //TODO does this view should handle touch events? or it should be only drawing pictures form adapter ?
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}