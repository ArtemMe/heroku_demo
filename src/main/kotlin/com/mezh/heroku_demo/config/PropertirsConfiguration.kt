package com.mezh.heroku_demo.config

import com.mezh.heroku_demo.config.BotConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(
        BotConfig::class
)
@Configuration
class PropertiesConfiguration