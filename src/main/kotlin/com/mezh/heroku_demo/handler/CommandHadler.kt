package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.handler.dto.CommandContext
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

interface CommandHadler {
    fun handle(context: CommandContext) : SendMessage
    fun getType() : Command
}