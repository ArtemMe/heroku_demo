package com.mezh.heroku_demo.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.telegram.telegrambots.bots.DefaultBotOptions

@Document
data class MessageDto (
        @Id
        var id: String? = null,
        var message: String? = null
)