package com.ivanovme.slice

import android.graphics.Color
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.ivanovme.slice.presentation.game.GameFragment
import com.ivanovme.slice.presentation.result.GameResultFragment

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, GameFragment.newInstance())
//                .commitNow()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GameResultFragment.newInstance())
                .commitNow()
        }


        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
    }
}
