package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.config.TaskConfig
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


@Service
class PingTask (
        val taskConfig: TaskConfig
){
    @Scheduled(fixedRateString = "\${ping-task.period}")
    fun pingMe() {
        try {
            val url = URL(taskConfig.url)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()
            connection.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}