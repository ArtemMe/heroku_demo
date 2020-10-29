package com.mezh.heroku_demo

import com.mezh.heroku_demo.config.BotConfig
import com.mezh.heroku_demo.services.DispatcherService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class TelegramHook(
        defaultBotOptions: DefaultBotOptions,
        val botConfig: BotConfig,
        val dispatcherService: DispatcherService
) : TelegramWebhookBot(defaultBotOptions) {

    override fun onWebhookUpdateReceived(update: Update?): BotApiMethod<*> {
        println("Receive message: " + update?.message)
        return handleUpdate(update)
    }

    fun handleUpdate(update: Update?) : SendMessage {
        if(update != null && update.hasCallbackQuery()) {
            return handleCallback(update.callbackQuery)
        }
        return handleInputMessage(update?.message)
    }

    private fun handleCallback(callbackQuery: CallbackQuery?) : SendMessage {
        return dispatcherService.createResponse(callbackQuery)
    }

    fun handleInputMessage(message: Message?) : SendMessage {
        return dispatcherService.createResponse(message)
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