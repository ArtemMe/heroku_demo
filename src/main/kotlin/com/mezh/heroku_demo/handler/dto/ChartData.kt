package com.mezh.heroku_demo.handler.dto

data class ChartData (
        val labels: List<String>,
        val datasets: List<DataSet>
)
