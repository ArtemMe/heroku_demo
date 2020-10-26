package com.mezh.heroku_demo.services


import com.mezh.heroku_demo.TestData.Companion.getMsgs
import com.mezh.heroku_demo.TestData.Companion.getMsgsWithWrongExercises
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.handler.dto.Treatment
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
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class ExerciseServiceTest {

    @Autowired
    lateinit var exerciseService: ExerciseService

    @Test
    fun `should return valid map with filled treatment`() {
        val result = exerciseService.collectTreatmentByExercises(getExerciseMap(), getMsgs())

        assert(result == getExpectedTreatment())
    }

    private fun getExerciseMap(): Map<String, MutableList<String>> {
        return mapOf(
                Pair("жим лежа" , mutableListOf()),
                Pair("присед" , mutableListOf())
        )
    }

    @Test
    fun `should return valid map with filled treatment if list messages contains wrong exercises`() {
        val result = exerciseService.collectTreatmentByExercises(getExerciseMap(), getMsgsWithWrongExercises())

        assert(result == getExpectedTreatment())
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

    private fun getExpectedTreatment() : MutableMap<String, MutableList<Treatment>>{
        return mutableMapOf(
                Pair("жим лежа" , mutableListOf(
                        Treatment(1, 10,20, LocalDateTime.of(2020, 1, 1, 1, 15)),
                        Treatment(2, 10,20, LocalDateTime.of(2020, 1, 1, 1, 30)),
                        Treatment(3, 10,20, LocalDateTime.of(2020, 1, 1, 1, 45))
                )),
                Pair("присед" , mutableListOf(
                        Treatment(1, 10,20, LocalDateTime.of(2020, 1, 1, 2, 15)),
                        Treatment(2, 10,20, LocalDateTime.of(2020, 1, 1, 2, 30))
                ))
        )
    }
}