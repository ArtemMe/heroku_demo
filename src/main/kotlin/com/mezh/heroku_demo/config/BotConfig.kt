package com.mezh.heroku_demo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.telegram.telegrambots.bots.DefaultBotOptions

@ConfigurationProperties(prefix = "telegrambot")
data class BotConfig (
        var webHookPath: String? = null,
        var userName: String? = null,
        var botToken: String? = null,

        var proxyType: DefaultBotOptions.ProxyType? = null,
        var proxyHost: String? = null,
        var proxyPort: Int = 0
)
