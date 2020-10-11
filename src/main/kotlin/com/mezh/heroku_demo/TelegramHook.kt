package com.mezh.heroku_demo

import com.mezh.heroku_demo.config.BotConfig
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class TelegramHook(
        defaultBotOptions: DefaultBotOptions,
        val botConfig: BotConfig
) : TelegramWebhookBot(defaultBotOptions) {

    override fun onWebhookUpdateReceived(update: Update?): BotApiMethod<*> {
        println("Receive message: " + update?.message)
        return handleUpdate(update)
    }

    fun handleUpdate(update: Update?) : SendMessage {
        return handleInputMessage(update?.message)
    }

    fun handleInputMessage(message: Message?) : SendMessage {
        val sendMessage = SendMessage()
        sendMessage.text = "dummy message response"
        sendMessage.chatId = message?.chatId.toString()
        return sendMessage
    }

    override fun getBotToken(): String? {
        return botConfig.botToken
    }

    override fun getBotPath(): String? {
        return botConfig.webHookPath
    }

    override fun getBotUsername(): String? {
        return botConfig.userName
    }
}