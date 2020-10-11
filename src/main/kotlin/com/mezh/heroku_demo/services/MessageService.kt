package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.dto.MessageDto
import com.mezh.heroku_demo.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.interceptor.MethodMapTransactionAttributeSource

@Service
class MessageService(
        val messageRepository: MessageRepository
) {
    fun saveMessage(text: String?) {
        messageRepository.save(
                MessageDto(message = text)
        )
    }

    fun findLastMessage() : String? {
        val messageList = messageRepository.findAll()
        if(messageList.isNotEmpty()) {
            return messageList.last().message
        }
        return "not found last message"
    }
}