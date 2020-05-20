package com.ivanovme.slice.presentation.game.deck

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.ivanovme.slice.presentation.screenWidth
import kotlin.math.abs

class DeckAnimator(
    private val deckView: DeckView,
    private val onSwiped: () -> (Unit)
) {
    companion object {
        private const val ANIMATION_DURATION = 500L
        private const val SCALE_MINIMUM = 0.8f
        private const val SCALE_COEFFICIENT = 500f
        private const val ROTATION_COEFFICIENT = 6.6f // TODO WTF 6.6?
        private const val ROTATION_MAX = 15f
    }

    private val maxXTranslation = 1.1f * screenWidth()
    private var isSwipeInProgress: Boolean = false
    private var onCardTranslationUpdated: ((Float, SwipeDirection) -> Unit)? = null

    //TODO rethink this
    private fun topCard(): View? = deckView.getChildAt(deckView.childCount - 1)
    private fun underneathCard(): View? = deckView.getChildAt(deckView.childCount - 2)

    //TODO SOLID (single responsibility) consideration, should animator actually compute directions and translations ? probably animator should just animate
    //TODO and someone else should calculate all animation data?

    fun animateDrag(diff: Float, direction: SwipeDirection) {
        val topCard = topCard()
        topCard?.translationX = diff

        val translationPercent = abs(diff) / maxXTranslation * 100
        topCard?.rotation =
            translationPercent / ROTATION_COEFFICIENT * if (direction == SwipeDirection.RIGHT) 1f else -1f

        val scale = SCALE_MINIMUM + translationPercent / SCALE_COEFFICIENT
        underneathCard()?.scaleX = scale
        underneathCard()?.scaleY = scale
        onCardTranslationUpdated?.invoke(translationPercent / 100f, direction)
    }

    fun animateFling(swipeDirection: SwipeDirection) {
        if (isSwipeInProgress)
            return
        when (swipeDirection) {
            SwipeDirection.LEFT -> leftSwipe()
            SwipeDirection.RIGHT -> rightSwipe()
        }
    }

    fun reverse() {
        val card = topCard() ?: return
        animateSwipe(
            card.translationX,
            0f,
            card.rotation,
            0f,
            if (underneathCard() == null) 0f else underneathCard()!!.scaleX,
            1f,
            true
        )
    }

    private fun rightSwipe() {
        val card = topCard() ?: return
        animateSwipe(
            card.translationX,
            maxXTranslation,
            card.rotation,
            ROTATION_MAX,
            if (underneathCard() == null) 0f else underneathCard()!!.scaleX,
            1f
        )
    }

    private fun leftSwipe() {
        val card = topCard() ?: return
        animateSwipe(
            card.translationX,
            -maxXTranslation,
            card.rotation,
            -ROTATION_MAX,
            if (underneathCard() == null) 0f else underneathCard()!!.scaleX,
            1f
        )
    }

    private fun animateSwipe(
        fromTranslation: Float,
        toTranslation: Float,
        fromRotation: Float,
        toRotation: Float,
        fromScale: Float,
        toScale: Float,
        fromReverse: Boolean = false
    ) {
        if (deckView.childCount == 0)
            return

        isSwipeInProgress = true

        val mutualDuration = computeAnimationDuration(abs(fromTranslation), maxXTranslation)

        val translationAnimator = ObjectAnimator.ofFloat(
            topCard(),
            "translationX",
            fromTranslation,
            toTranslation
        ).apply {
            duration = mutualDuration
        }

        val rotationAnimator = ObjectAnimator.ofFloat(
            topCard(),
            "rotation",
            fromRotation,
            toRotation
        ).apply {
            duration = mutualDuration

        }

        //UNDERNEATH CARDS
        var scaleXAnimator: ObjectAnimator? = null
        var scaleYAnimator: ObjectAnimator? = null

        if (underneathCard() != null) {
            scaleXAnimator = ObjectAnimator.ofFloat(
                underneathCard(),
                "scaleX",
                fromScale,
                toScale
            ).apply {
                duration = mutualDuration
            }

            scaleYAnimator = ObjectAnimator.ofFloat(
                underneathCard(),
                "scaleY",
                fromScale,
                toScale
            ).apply {
                duration = mutualDuration
            }
        }

        AnimatorSet().apply {
            when {
                underneathCard() == null -> playTogether(translationAnimator, rotationAnimator)
                else -> playTogether(
                    translationAnimator,
                    rotationAnimator,
                    scaleXAnimator,
                    scaleYAnimator
                )
            }
            start()
        }.addListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animator?) {
                isSwipeInProgress = false
                if (!fromReverse)
                    onSwiped()
            }
        })

//        translationAnimator.addUpdateListener { valueAnimator ->
//            val updatedValue = if (abs(fromTranslation) < abs(toTranslation)) {
//                valueAnimator.animatedFraction
//            } else {
//                1 - valueAnimator.animatedFraction
//            }
//            println("transaction animator : updated value = $updatedValue")
//            onCardTranslationUpdated?.invoke(updatedValue)
//        }

    }

    private fun computeAnimationDuration(current: Float, max: Float) =
        (ANIMATION_DURATION * percent(current, max)).toLong()

    private fun percent(current: Float, max: Float) = (max - current) / max

    fun addAnimationUpdateListener(onValueUpdateHandler: (Float, SwipeDirection) -> Unit) {
        onCardTranslationUpdated = onValueUpdateHandler
    }

}

//TODO move this to appropriate package
fun Float.directionTo(toX: Float) =
    if (toX > this) {
        SwipeDirection.RIGHT
    } else {
        SwipeDirection.LEFT
    }

enum class SwipeDirection {
    LEFT,
    RIGHT
}