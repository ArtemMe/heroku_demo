package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.StateType
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.ButtonService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class StartCommandHandler (
        private val buttonService: ButtonService
) : CommandHadler{
    private val COMMAND_TYPE: Command = Command.START

    override fun handle(context: CommandContext): SendMessage {
        return sendResponseWithButtons(context.message)
    }

    private fun sendResponseWithButtons(message: Message?): SendMessage {
        return SendMessage()
                .setChatId(message?.chatId)
                .setText("Пример")
                .setReplyMarkup(buttonService.createButtons())
    }

    override fun getType(): Command {
        return COMMAND_TYPE
    }

    override fun getStateType(): StateType {
        return StateType.START
    }

}