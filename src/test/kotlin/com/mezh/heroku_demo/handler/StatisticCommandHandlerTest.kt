package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.TestData.Companion.getMsgs
import com.mezh.heroku_demo.TestData.Companion.getMsgsWithWrongExercises
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.repository.MessageRepository
import com.mezh.heroku_demo.services.QuickChartService
import com.mezh.heroku_demo.services.UserService
import com.nhaarman.mockitokotlin2.any
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

import com.nhaarman.mockitokotlin2.whenever
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.util.*

@SpringBootTest
class StatisticCommandHandlerTest {

    @Autowired
    lateinit var statisticCommandHandler: StatisticCommandHandler
    @MockBean
    lateinit var messageRepository: MessageRepository
    @MockBean
    lateinit var quickChartService: QuickChartService

    @Test
    fun `should return valid statistic`() {
        whenever(messageRepository.findAll()).thenReturn(getMsgs())
        whenever(quickChartService.createChart(any(), any(), any())).thenReturn("some url")

        val result = statisticCommandHandler.handle(createContext())

        assert(!result.text.isNullOrBlank())
    }

    @Test
    fun `should return valid statistic if list messages contains wrong exercises`() {
        whenever(messageRepository.findAll()).thenReturn(getMsgsWithWrongExercises())
        whenever(quickChartService.createChart(any(), any(), any())).thenReturn("some url")

        val result = statisticCommandHandler.handle(createContext())

        assert(!result.text.isNullOrBlank())
    }

    private fun createContext(): CommandContext {
        return CommandContext(createInputMsg(), getUser())
    }

    private fun createInputMsg(): Message {
        val msg = mock(Message::class.java)
        `when`(msg.from).thenReturn(User(100, "", false, "", "", ""))

        return msg
    }

    private fun getUser(): UserEntity {
        return UserEntity(
                userId = "100",
                exercisesList = setOf(TrainingComplexEntity("Комплекс 1", setOf("жим лежа", "присед", "пресс"))),
                currentState = null
        )
    }


}