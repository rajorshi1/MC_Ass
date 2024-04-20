package com.example.sensor
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SensorRepository(private val sensorDao: SensorDao) {

    val sensorData: Flow<List<SensorData>> = sensorDao.getSensorData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(sensorData: SensorData) {
        sensorDao.insert(sensorData)
    }
}