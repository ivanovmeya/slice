package com.ivanovme.slice.presentation.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanovme.slice.R
import kotlinx.android.synthetic.main.fragment_game_result.*

class GameResultFragment : Fragment(R.layout.fragment_game_result) {

    companion object {
        fun newInstance(): Fragment = GameResultFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        titleView.text = "Title"
        taskView.text = "Result"
        gameResultView.text = "7/12"

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter =
    }
}