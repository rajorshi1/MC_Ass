package com.example.sensor
import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class SensorViewModel(private val repository: SensorRepository) : ViewModel() {
    val sensorData: LiveData<List<SensorData>> = repository.sensorData.asLiveData()

    fun insert(sensorData: SensorData) = viewModelScope.launch {
        repository.insert(sensorData)
    }
}

class SensorViewModelFactory(private val repository: SensorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SensorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class sensorApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { SensorDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { SensorRepository(database.sensorDao()) }
    override fun onCreate() {
        super.onCreate()
        Thread { database.clearAllTables() }.start()
    }
}
