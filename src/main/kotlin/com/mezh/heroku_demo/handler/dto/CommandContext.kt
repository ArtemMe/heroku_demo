package com.mezh.heroku_demo.handler.dto

import org.telegram.telegrambots.meta.api.objects.Message

data class CommandContext(
        val message: Message
)