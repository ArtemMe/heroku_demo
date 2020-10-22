package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.handler.dto.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class QuickChartService {

    fun createChart(x: List<String>, y: List<Int>, chartName: String) : String? {
        val restTemplate = RestTemplate()
        val response: ResponseEntity<String> = restTemplate.postForEntity(
                        QUICK_CHART_URL,
                        buildRequest(x, y, chartName),
                        String::class.java)

        return response.body
    }

    fun buildRequest(labels: List<String>, data: List<Int>, chartName: String) : HttpEntity<QuickChartRequest> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        return HttpEntity(
                        buildQuickChartRequest(labels, data, chartName),
                        headers)
    }

    fun buildQuickChartRequest(labels: List<String>, data: List<Int>, chartName: String) : QuickChartRequest {
        return QuickChartRequest(
                chart = Chart(
                        type = ChartType.LINE.type,
                        data = ChartData(
                                labels = labels,
                                datasets = listOf(DataSet(
                                        label = chartName,
                                        data = data
                                ))
                        )
                )
        )
    }

    companion object {
        val QUICK_CHART_URL = "https://quickchart.io/chart/create"
    }
}