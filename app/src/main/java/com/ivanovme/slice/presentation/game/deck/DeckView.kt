package com.ivanovme.slice.presentation.game.deck

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import com.ivanovme.slice.R
import com.ivanovme.slice.presentation.game.Question
import com.ivanovme.slice.presentation.game.deck.gesturedetector.DragFinishedGestureDetector
import com.ivanovme.slice.presentation.game.deck.gesturedetector.OnDragGestureHandler
import kotlin.math.abs


/**
 * Deck of cards with some data provider (like adapter in recycler view)
 * support horizontal swipe and drag gestures.
 * provide callback for drag gesture (to update scale of answers buttons)
 * provide callback gesture finished (success drag/swipe to left or right)
 * intercepts touch event
 *
 * deck's top card scale and rotation on swipe or drag gesture
 * deck's second card scale on swipe or drag gesture
 */
class DeckView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    companion object {
        const val THRESHOLD = 300
        const val VELOCITY_THRESHOLD = 0.15
    }

    private var lastLoadedQuestion = 0

    //TODO WHY ADAPTER NEEDs A CONTEXT ?
    private val adapter = DeckAdapter(context)

    private val maxFlingVelocity =
        ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()

    private val gestureDetector =
        DragFinishedGestureDetector(context, object : OnDragGestureHandler {
            override fun onDrag(origin: MotionEvent, current: MotionEvent) {
                val diff = current.rawX - origin.rawX
                val direction = origin.rawX.directionTo(current.rawX)
                deckAnimator.animateDrag(diff, direction)
            }

            override fun onFling(origin: MotionEvent, current: MotionEvent, velocityX: Float) =
                processFling(origin, current, velocityX)

            override fun onDragFinished(origin: MotionEvent, last: MotionEvent) =
                processFling(origin, last)

            private fun processFling(
                origin: MotionEvent,
                current: MotionEvent,
                velocityX: Float = 0f
            ) {
                val direction = origin.rawX.directionTo(current.rawX)
                val velocityPercent = velocityX / maxFlingVelocity
                val distance = current.rawX - origin.rawX
                if (abs(distance) > THRESHOLD || abs(velocityPercent) > VELOCITY_THRESHOLD) {
                    deckAnimator.animateFling(direction)
                } else {
                    deckAnimator.reverse()
                }
            }
        })

    private val deckAnimator = DeckAnimator(
        deckView = this,
        onSwiped = {
            if (childCount != 0) {
                val view = getChildAt(childCount - 1)
                removeView(view)
                lastLoadedQuestion++
                if (lastLoadedQuestion < adapter.count) {
                    val newView = adapter.getView(lastLoadedQuestion, view, this)
                    adapter.bindView(newView, lastLoadedQuestion)
                    this.post {
                        addView(newView, 0)
                    }
                }
            } else {
                //TODO HANDLE END OF THE GAME
                println("THE END")
            }
        }
    )

    init {
        setOnTouchListener(gestureDetector)
        addView(adapter.getView(0, null, this@DeckView))
        addView(adapter.getView(1, null, this@DeckView))

        this.viewTreeObserver.apply {
            addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    this@DeckView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    adapter.bindView(getChildAt(0), 0)
                    adapter.bindView(getChildAt(1), 1)
                    lastLoadedQuestion = 3
                }
            })
        }
    }

    fun performSwipe(direction: SwipeDirection) {
        deckAnimator.animateFling(direction)
    }

    fun addCardTranslationUpdateListener(onValueUpdateHandler: (Float, SwipeDirection) -> Unit) =
        deckAnimator.addAnimationUpdateListener(onValueUpdateHandler)


    fun setData(characters: List<Question>) {
        adapter.clear()
        adapter.addAll(characters)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return gestureDetector.onIntercept(ev) || super.onInterceptTouchEvent(ev)
    }

}