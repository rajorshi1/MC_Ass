package com.example.assignment_q2

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).build()

            val weatherDao = db.weatherDao()
            var date by remember { mutableStateOf("") }
            var error by remember { mutableStateOf<String?>(null) }
            var weatherData by remember { mutableStateOf<WeatherData?>(null) }
            var averageWeatherData by remember { mutableStateOf<AverageWeatherData?>(null) }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.weather),
                        contentScale = ContentScale.FillBounds
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Box (modifier = Modifier
                    .background(
                        color = Color.Gray.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Weather App",
                        color = Color.White,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
                        fontSize = 30.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Enter date:") },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    textStyle = TextStyle(color = Color.Black.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        try {
                            val inputDate = dateFormat.parse(date)
                            val currentDate = Date()
                            error = null

                            if (inputDate != null) {
                                if (inputDate.after(currentDate)) {
                                    weatherData = null
                                    val earlyDate = LocalDate.parse(date).minusYears(10)
                                    val finalDate = earlyDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                    val averageLiveData = weatherDao.getAverage(date, finalDate)
                                    averageLiveData.observeForever { averageData ->
                                        averageWeatherData = averageData
                                    }
                                }
                                else {
                                    averageWeatherData = null
                                    val localDataLiveData = weatherDao.getData(date)
                                    localDataLiveData.observeForever { localData ->
                                        if (localData != null) {
                                            weatherData = localData
                                        }
                                        else {
                                            val connectivityManager =
                                                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                            val activeNetworkInfo = connectivityManager.activeNetworkInfo
                                            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    val data = fetchWeatherData(date)
                                                    if (data != null) {
                                                        weatherDao.insert(data)
                                                        weatherData = data
                                                    } else {
                                                        error = "Network request failed."
                                                    }
                                                }
                                            }
                                            else {
                                                error = "No network."
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            error = "Invalid date format."
                            weatherData = null
                        }
                    },
                ) {
                    Text("🔍")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Timbuktu",
                    style = TextStyle(fontSize = 30.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White),
                    )
                Error(error)
                weatherData?.let { data ->
                    WeatherDataColumn(data.minTemp, data.maxTemp, false)
                }
                averageWeatherData?.let { data ->
                    WeatherDataColumn(data.minTemp, data.maxTemp, true)
                }
            }
        }
    }
}

@Composable
fun Error(errorMessage: String?) {
    errorMessage?.let {
        Text(
            text = "Error: $it",
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherDataColumn(minTemp: Float, maxTemp: Float, isAfter: Boolean) {
    val label = if (isAfter) "Avg Max" else "Max"
    val label1 = if (isAfter) "Avg Min" else "Min"
    val minTempCelsius = fahrenheitToCelsius(minTemp)
    val maxTempCelsius = fahrenheitToCelsius(maxTemp)
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "$label: ${"%.2f".format(maxTempCelsius)} °C",
            style = TextStyle(fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Normal)
        )
        Text(
            text = "$label1: ${"%.2f".format(minTempCelsius)} °C",
            style = TextStyle(fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Normal)
        )
    }
}

// Convert Fahrenheit to Celsius
fun fahrenheitToCelsius(fahrenheit: Float): Float {
    return (fahrenheit - 32) * 5 / 9
}

suspend fun fetchWeatherData(date: String): WeatherData? {
    val url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Timbuktu/$date?unitGroup=us&key=CJZMLZMSK8BB53XR3DM6EQJJN"
    val request = Request.Builder().url(url).build()

    return suspendCancellableCoroutine { continuation ->
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    val daysArray = jsonObject.getJSONArray("days")
                    val weatherObject = daysArray.getJSONObject(0)
                    val maxTemp = weatherObject.getDouble("tempmax").toFloat()
                    val minTemp = weatherObject.getDouble("tempmin").toFloat()
                    val weatherData = WeatherData(date, maxTemp, minTemp)
                    continuation.resume(weatherData)
                } catch (e: Exception) {
                    continuation.resume(null)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                continuation.resume(null)
            }
        })

        continuation.invokeOnCancellation {
            client.dispatcher.cancelAll()
        }
    }
}
