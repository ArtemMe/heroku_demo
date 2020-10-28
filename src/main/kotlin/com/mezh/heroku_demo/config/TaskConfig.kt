package com.mezh.heroku_demo.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ping-task")
data class TaskConfig (
        var url: String?
)