package com.plcoding.classifying.domain

import android.graphics.Bitmap

interface LandmarkClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}

data class Classification(
    val name: String,
    val score: Float
)
