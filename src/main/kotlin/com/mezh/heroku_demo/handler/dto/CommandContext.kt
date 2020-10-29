package com.mezh.heroku_demo.handler.dto

import com.mezh.heroku_demo.entity.UserEntity
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.*

data class CommandContext(
        val message: Message,
        val user: UserEntity
) {
    var callbackQuery: CallbackQuery? = null
}