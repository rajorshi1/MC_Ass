package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Stop(val name: String, val distance: Int)

val stopList = listOf(
    Stop("Stop 1", 10),
    Stop("Stop 2", 20),
    Stop("Stop 3", 30),
    Stop("Stop 4", 40),
    Stop("Stop 5", 50),
    Stop("Stop 6", 60),
    Stop("Stop 7", 70),
    Stop("Stop 8", 80),
    Stop("Stop 9", 90),
    Stop("Stop 10", 100)
)

val lazyStopList = listOf(
    Stop("Stop 1", 10),
    Stop("Stop 2", 20),
    Stop("Stop 3", 30),
    Stop("Stop 4", 40),
    Stop("Stop 5", 50),
    Stop("Stop 6", 60),
    Stop("Stop 7", 70),
    Stop("Stop 8", 80),
    Stop("Stop 9", 90),
    Stop("Stop 10", 100),
    Stop("Stop 11", 110),
    Stop("Stop 12", 120),
    Stop("Stop 13", 130),
    Stop("Stop 14", 140),
    Stop("Stop 15", 150),
    Stop("Stop 16", 160),
    Stop("Stop 17", 170),
    Stop("Stop 18", 180),
    Stop("Stop 19", 190),
    Stop("Stop 20", 200)
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var displayMiles by remember {
                mutableStateOf(false)
            }
            var lazyOrNot = false
            Column (){
                Box (modifier = Modifier
                    .background(color = Color.Blue)
                    .fillMaxWidth()
                    .fillMaxHeight(0.05f)
                ) {
                    Text(text = "Distance Tracker",
                        color = Color.White,
                        fontSize = 30.sp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (lazyOrNot) LazyList(displayMiles) else OrdinaryList(displayMiles)
                    Button(
                        onClick = { displayMiles = !displayMiles }
                    ) {
                        Text(text = if (displayMiles) "Show Kilometers" else "Show Miles")
                    }
                }
                Progress(displayMiles, lazyOrNot)
            }
        }
    }



@Composable
fun OrdinaryList(displayMiles: Boolean) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(0.5f)
            .padding(20.dp)
            .fillMaxHeight(0.5f),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        stopList.forEach { stop ->
            Text(text = stop.name)
            val distance = if (displayMiles) stop.distance * 0.621371 else stop.distance.toDouble()
            val formattedDistance = "%.2f".format(distance)
            Text(text = "Distance: $formattedDistance ${if (displayMiles) "miles" else "km"}")
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
        }
    }
}

@Composable
fun LazyList(displayMiles: Boolean) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.5f)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        items(lazyStopList) { stop ->
            Text(text = stop.name)
            val distance = if (displayMiles) stop.distance * 0.621371 else stop.distance.toDouble()
            val formattedDistance = "%.2f".format(distance)
            Text(text = "Distance: $formattedDistance ${if (displayMiles) "miles" else "km"}")
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
        }
    }
}

@Composable
fun Progress(displayMiles: Boolean, lazyOrNot: Boolean) {
    val total = if (lazyOrNot) 200 else 100
    var distanceCovered by remember { mutableStateOf(0) }
    var distanceLeft by remember { mutableStateOf(if (lazyOrNot) 200 else 100) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(color = Color.LightGray)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val covered =
                if (displayMiles) distanceCovered * 0.621371 else distanceCovered.toDouble()
            val formattedCovered = "%.2f".format(covered)
            val left = if (displayMiles) distanceLeft * 0.621371 else distanceLeft.toDouble()
            val formattedLeft = "%.2f".format(left)
            Text(text = "Covered: $formattedCovered ${if (displayMiles) "miles" else "km"}")
            Text(text = "Left: $formattedLeft ${if (displayMiles) "miles" else "km"}")
            Button(
                onClick = {
                    if (distanceCovered < total && distanceLeft > 0) {
                        distanceCovered += 10
                        distanceLeft -= 10
                    }
                }
            ) {
                Text(text = "Next")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = distanceCovered / if (lazyOrNot) 200f else 100f,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Blue
        )
    }
    }
}



