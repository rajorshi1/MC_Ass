package com.example.assignment_q2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherData(
    @PrimaryKey val date: String,
    val maxTemp: Float,
    val minTemp: Float
)
