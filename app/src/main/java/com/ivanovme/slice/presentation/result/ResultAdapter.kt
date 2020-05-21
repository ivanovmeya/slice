package com.ivanovme.slice.presentation.result

import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ivanovme.slice.R
import kotlinx.android.synthetic.main.item_result.view.*

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    private val results: List<ResultModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_result, parent, false)
        )
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {

    }

    inner class ResultViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val name = view.nameView
        private val isAnswerCorrect = view.isAnswerCorrectView
        private val answer = view.answerView
        private val rightAnswer = view.rightAnswerView

        fun bind(model: ResultModel) {
            val context = isAnswerCorrect.context
            name.text = model.name
            isAnswerCorrect.text = if (model.correctAnswer == null) {
                context.getText(R.string.wrong)
            } else {
                context.getText(R.string.correct)
            }
            rightAnswer.isVisible = model.correctAnswer == null
            if (model.correctAnswer != null) {
                rightAnswer.text = model.correctAnswer
                answer.paintFlags = answer.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            } else {
                //TODO
//                answer.paintFlags =  answer.paintFlags  and (~STRIKE_THRU_TEXT_FLAG)
            }
            answer.text = model.answer

        }
    }
}