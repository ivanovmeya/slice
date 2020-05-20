package com.ivanovme.slice.presentation.game.deck

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.ivanovme.slice.R
import com.ivanovme.slice.presentation.game.Question
import java.io.File
import java.io.InputStream


class DeckAdapter(context: Context) : ArrayAdapter<Question>(context, 0) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView != null) {
            convertView.rotation = 0f
            convertView.translationX = 0f
        }

        return convertView
            ?: LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character_deck, parent, false)

    }

    fun bindView(view: View, position: Int) {
        val question = getItem(position)
        if (question != null) {
            val nameView = view.findViewById<TextView>(R.id.nameView)
            val cardRootView = view.findViewById<ConstraintLayout>(R.id.cardRootView)
            nameView.text = question.name
            cardRootView.background =
                loadImage(question.image, cardRootView.measuredWidth, cardRootView.measuredHeight)

        }
    }

    private fun loadImage(assetName: String, targetWidth: Int, targetHeight: Int): Drawable {
        //TODO can throw NPE exception
        //TODO crop on face (crop on top of heights pics, and in center of width pics)
        val ims: InputStream = context.assets.open("$assetName.jpg")

       //extract bmp transformation logic from utils, and transform with croping on top of tall pictures (where usually the face is)
        val centeredBmp = ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeStream(ims),
            targetWidth,
            targetHeight,
            ThumbnailUtils.OPTIONS_RECYCLE_INPUT
        )

        return RoundedBitmapDrawableFactory.create(context.resources, centeredBmp).apply {
            cornerRadius = centeredBmp.width * 0.06f
        }

    }
}