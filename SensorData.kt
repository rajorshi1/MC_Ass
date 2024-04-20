package com.example.sensor
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SensorData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val x: Float,
    val y: Float,
    val z: Float
)