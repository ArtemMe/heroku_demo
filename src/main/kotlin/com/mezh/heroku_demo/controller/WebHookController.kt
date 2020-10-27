package com.mezh.heroku_demo.controller

import com.mezh.heroku_demo.TelegramHook
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

@RestController
class WebHookController(val telegramHook: TelegramHook) {

    @RequestMapping(value = ["/"], method = [RequestMethod.POST])
    fun onUpdateReceived(@RequestBody update: Update?): BotApiMethod<*> {
        return telegramHook.onWebhookUpdateReceived(update)
    }
}