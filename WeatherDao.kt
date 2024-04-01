package com.example.assignment_q2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatherdata WHERE date = :date")
    fun getData(date: String): LiveData<WeatherData?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherData: WeatherData)

    @Query("SELECT AVG(maxTemp) as maxTemp, AVG(minTemp) as minTemp FROM weatherdata WHERE date < :date and date > :current ")
    fun getAverage(date: String, current: String): LiveData<AverageWeatherData?>
}
