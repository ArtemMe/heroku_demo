package com.mezh.heroku_demo.controller

import com.mezh.heroku_demo.BaseIntegrationTest
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.repository.MessageRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class WebHookControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Test
    fun `mongo repository should work` () {
        messageRepository.save(MessageDto("1", 1, "hello", 12345678))
        val result = messageRepository.findById("1")

        assert(result.isPresent)
    }
}