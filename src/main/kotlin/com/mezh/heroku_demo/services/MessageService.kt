package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.dto.MessageDto
import com.mezh.heroku_demo.repository.MessageRepository
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class MessageService(
        private val messageRepository: MessageRepository,
        private val buttonService: ButtonService
) {
    fun saveMessage(message: Message?) {
        messageRepository.save(
                MessageDto(
                        id = message?.messageId?.toInt(),
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

    fun getUserId(message: Message?): String? {
        return message?.from?.userName
    }

    fun sendResponseWithButtons(message: Message?): SendMessage {
        return SendMessage()
                .setChatId(message?.chatId)
                .setText("Пример")
                .setReplyMarkup(buttonService.createButtons())
    }

    fun createResponse(message: Message?): SendMessage {
        this.saveMessage(message)
        val messageText = message?.text

        if (messageText.toString().trim() == "/start") {
            return sendResponseWithButtons(message)
        } else {
            return replyLastMessage(message)
        }
    }

    fun replyLastMessage(message: Message?): SendMessage {
        val sendMessage = SendMessage()
        sendMessage.text = this.findLastMessage()
        sendMessage.chatId = message?.chatId.toString()
        return sendMessage
    }


}