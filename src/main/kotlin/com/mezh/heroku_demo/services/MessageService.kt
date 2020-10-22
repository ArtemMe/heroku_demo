package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.repository.MessageRepository
import org.springframework.stereotype.Service

@Service
class MessageService(
        private val messageRepository: MessageRepository
)  {
    fun getMessage(userId: Int) : List<MessageDto> {
        return messageRepository.findAll()
                .filter { messageDto -> messageDto.userId == userId }
                .sortedBy { messageDto -> messageDto.dateTime }
    }
}