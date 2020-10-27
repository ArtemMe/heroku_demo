package com.mezh.heroku_demo.controller

import com.mezh.heroku_demo.BaseIntegrationTest
import com.mezh.heroku_demo.StringLoader
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.repository.MessageRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class WebHookControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should successful save to mongo` () {
        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType("application/json")
                .content(StringLoader.fromClasspath("/__files/telegram_req.json")))
                .andExpect(MockMvcResultMatchers.status().isOk)

        val result = messageRepository.findById("427")

        assert(result.isPresent)
    }
}