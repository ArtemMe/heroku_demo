package com.mezh.heroku_demo.services

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup

@Service
class MenuService {

    fun createMessageWithKeyboard(chatId: Long,
                                  textMessage: String,
                                  replyKeyboardMarkup: ReplyKeyboardMarkup?): SendMessage {
        val sendMessage = SendMessage()
        sendMessage.enableMarkdown(true)
        sendMessage.setChatId(chatId)
        sendMessage.text = textMessage
        if (replyKeyboardMarkup != null) {
            sendMessage.replyMarkup = replyKeyboardMarkup
        }
        return sendMessage
    }
}