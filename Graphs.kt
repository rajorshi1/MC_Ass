package com.example.sensor

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.combinedchart.CombinedChart
import co.yml.charts.ui.combinedchart.model.CombinedChartData
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle


class Graphs : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                val sensorData by viewModel.sensorData.observeAsState(emptyList())

                if (sensorData.isEmpty()) {
                    return@Column
                }
                CombinedChart(
                    modifier = Modifier.height(400.dp),
                    combinedChartData = CombinedChartData(
                        combinedPlotDataList = listOf(
                            LinePlotData(
                            lines = listOf(
                                Line(
                                    dataPoints =
                                    sensorData.mapIndexed { index, data ->
                                        Point(index.toFloat(), data.x)
                                    },
                                    LineStyle(color = Color.Blue),
                                ),
                                Line(
                                    dataPoints =
                                    sensorData.mapIndexed { index, data ->
                                        Point(index.toFloat(), data.y)
                                    },
                                    LineStyle(color = Color.Green)
                                )
                            ),
                        ),
                            LinePlotData(
                            lines = listOf(
                                Line(
                                    dataPoints =
                                    sensorData.mapIndexed { index, data ->
                                        Point(index.toFloat(), data.z)
                                    },
                                    LineStyle(color = Color.Red),
                                )
                            ),
                        )),
                        xAxisData =
                        AxisData.Builder()
                            .axisStepSize(100.dp)
                            .backgroundColor(Color.Transparent)
                            .steps(sensorData.mapIndexed { index, data ->
                                Point(index.toFloat(), data.x)
                            }.size - 1)
                            .labelData { i -> i.toString() }
                            .labelAndAxisLinePadding(15.dp)
                            .build(),
                        yAxisData = AxisData.Builder()
                            .backgroundColor(Color.Transparent)
                            .labelAndAxisLinePadding(20.dp)
                            .labelData { i ->
                                val yScale = 20
                                (i * yScale).toString()
                            }.build()
                    )
                )
            }
        }
    }
}
