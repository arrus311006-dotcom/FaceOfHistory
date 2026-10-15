package com.example.historyquizru.util

import android.widget.ImageView
import coil.load
import com.example.historyquizru.R

object ImageLoader {
    fun loadPersonalityImage(imageView: ImageView, imageName: String) {
        imageView.load("file:///android_asset/$imageName") {
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_error)
        }
    }
}