package com.ivanovme.slice.presentation.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ivanovme.slice.R
import com.ivanovme.slice.presentation.game.deck.SwipeDirection
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment(R.layout.fragment_game) {

    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        setupOnClickListeners()
        renderGame(createTestGame())
    }

    private fun setupOnClickListeners() {
        firstOptionView.setOnClickListener {
            deckView.performSwipe(SwipeDirection.LEFT)
        }
        secondOptionView.setOnClickListener {
            deckView.performSwipe(SwipeDirection.RIGHT)
        }

        deckView.addCardTranslationUpdateListener { valueFraction, swipeDirection ->
            //scale button
            val additionalScale = 0.5f
            val maxScale = 1.5f // on 40 % of cardTranslation

            val scale = (1.0f + valueFraction * 100 * additionalScale / 40).coerceAtMost(maxScale)
            if (swipeDirection == SwipeDirection.LEFT) {
                firstOptionView.scaleX = scale
                firstOptionView.scaleY = scale
            } else {
//                swipeDirection == SwipeDirection.RIGHT
                secondOptionView.scaleX = scale
                secondOptionView.scaleY = scale
            }
        }
    }

    private fun renderGame(state: GameViewState) {
        firstOptionView.text = state.firstOption
        secondOptionView.text = state.secondOption
        titleView.text = state.title
        taskView.text = state.task
        deckView.setData(state.questions)
    }

    private fun createTestGame(): GameViewState {

        val questions = listOf(
            Question("Джон Сноу", "1.John"),
            Question("Давос", "4.Davos"),
            Question("Семвел Тарли", "5.Semwell"),
            Question("Мирцелла", "9.Mircella"),
            Question("Лиана", "10.Liana"),
            Question("Фродо Беггинс", "2.Frodo"),
            Question("Эовин", "3.Eovin"),
            Question("Арагорн", "6.Aragorn"),
            Question("Арвен", "7.Arven"),
            Question("Эомер", "8.Eomer")
        )

        return GameViewState(
            title = getString(R.string.title),
            task = getString(R.string.split_characters),
            questions = questions,
            firstOption = getText(R.string.game_of_thrones).toString(),
            secondOption = getText(R.string.lord_of_the_rings).toString()
        )
    }

}