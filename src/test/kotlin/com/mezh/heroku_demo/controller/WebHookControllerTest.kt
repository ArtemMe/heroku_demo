package com.mezh.heroku_demo.controller

import com.mezh.heroku_demo.BaseIntegrationTest
import com.mezh.heroku_demo.StringLoader
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.repository.MessageRepository
import com.mezh.heroku_demo.repository.UserRepository
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class WebHookControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var messageRepository: MessageRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should successful save to mongo`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType("application/json")
                .content(StringLoader.fromClasspath("/__files/telegram_req.json")))
                .andExpect(MockMvcResultMatchers.status().isOk)

        val result = messageRepository.findById("427")

        assert(result.isPresent)
    }

    @Test
    fun `should successful create chart`() {
        generateMessageRecords()
        generateUserRecord()

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType("application/json")
                .content(StringLoader.fromClasspath("/__files/telegram_stat_req.json")))

        result
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("\$.text", Matchers.containsString("{\"success\":true")))
    }

    fun generateMessageRecords() {
        messageRepository.saveAll(listOf(
                MessageDto("1", 100, "жим лежа", 1577840400),
                MessageDto("2", 100, "20:10", 1577841300),
                MessageDto("3", 100, "20:10", 1577842200),
                MessageDto("4", 100, "20:10", 1577843100),
                MessageDto("5", 100, "присед", 157784000),
                MessageDto("6", 100, "20:10", 1577844900),
                MessageDto("7", 100, "20:10", 1577845800),
                // второй день
                MessageDto("8", 100, "жим лежа", 1577926800),
                MessageDto("9", 100, "30:10", 1577927700),
                MessageDto("10", 100, "30:10", 1577928600),
                MessageDto("11", 100, "40:10", 1577929500),
                MessageDto("12", 100, "присед", 1577930400),
                MessageDto("13", 100, "20:10", 1577931300),
                MessageDto("14", 100, "30:10", 1577932200)
        ))
    }

    fun generateUserRecord() {
        userRepository.save(UserEntity(
                "100",
                exercisesList = setOf(TrainingComplexEntity("Complex_1", setOf("жим лежа", "присед"))),
                currentState = null
        ))
    }
}