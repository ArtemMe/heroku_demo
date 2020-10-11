package com.mezh.heroku_demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext

@Configuration
class HookConfig(
       val botConfig: BotConfig
) {

    @Bean
    fun defaultBotOptions(): DefaultBotOptions {
        val options = ApiContext
                .getInstance(DefaultBotOptions::class.java)
        options.proxyHost = botConfig.proxyHost
        options.proxyPort = botConfig.proxyPort
        options.proxyType = botConfig.proxyType
        return options
    }
}