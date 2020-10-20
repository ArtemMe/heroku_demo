package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.handler.CommandHadler
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.repository.MessageRepository
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class MessageService(
        private val messageRepository: MessageRepository,
        private val commandHandlers: List<CommandHadler>
) {
    fun saveMessage(message: Message?) {
        messageRepository.save(
                MessageDto(
                        id = message?.messageId?.toInt().toString(),
                        message = message?.text,
                        userId = getUserId(message),
                        dateTime = message?.date?.toInt()
                )
        )
    }

    fun findLastMessage(): String? {
        val messageList = messageRepository.findAll()
        if (messageList.isNotEmpty()) {
            return messageList.last().message
        }
        return "not found last message"
    }

    fun getUserId(message: Message?): Int? {
        return message?.from?.id
    }

    fun createResponse(message: Message?): SendMessage {
        this.saveMessage(message)
        val messageText = message?.text

        if (messageText.toString().trim() == Command.START.desc) {
            return getHandler(Command.START).handle(CommandContext(message!!))
        } else if (messageText.toString().contains(Command.ADD_EXERCISES.desc, true)) {
            return getHandler(Command.ADD_EXERCISES).handle(CommandContext(message!!))
        } else if (messageText.toString().contains(Command.STATISTIC.desc, true)) {
            return getHandler(Command.STATISTIC).handle(CommandContext(message!!))
        }else {
            return replyLastMessage(message)
        }
    }

    fun replyLastMessage(message: Message?): SendMessage {
//        val sendMessage = SendMessage()
//        sendMessage.text = this.findLastMessage()
//        sendMessage.chatId = message?.chatId.toString()
//        return sendMessage
        return SendMessage()
    }

    fun getHandler(command: Command) : CommandHadler {
        return commandHandlers
                .first { handler -> handler.getType() == command }
    }

}