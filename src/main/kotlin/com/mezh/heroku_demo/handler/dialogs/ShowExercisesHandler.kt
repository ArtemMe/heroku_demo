package com.mezh.heroku_demo.handler.dialogs

import com.mezh.heroku_demo.entity.StateType
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.exceptions.ExceptionType
import com.mezh.heroku_demo.exceptions.HandlerException
import com.mezh.heroku_demo.handler.DialogHandler
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.ChartService
import com.mezh.heroku_demo.services.ExerciseService
import com.mezh.heroku_demo.services.MessageService
import com.mezh.heroku_demo.services.QuickChartService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class ShowExercisesHandler(
        private val messageService: MessageService,
        private val quickChartService: QuickChartService,
        private val chartService: ChartService,
        private val exerciseService: ExerciseService
) : DialogHandler {

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message, context.callbackQuery, context.user)
    }

    private fun handleInternal(message: Message, callbackQuery: CallbackQuery?, user: UserEntity): SendMessage {

        val prevState = user.currentState!!.type

        if(prevState == StateType.SHOWING_EXE) {
            val sendMessage = handleInputExercises(callbackQuery, user)
            return sendMessage
        }

        return SendMessage()
                .setChatId(message.chatId)
                .setText("Неизвестное состояние...")
    }


    private fun handleInputExercises(callbackQuery: CallbackQuery?, user: UserEntity): SendMessage {
        if(callbackQuery == null) return SendMessage()

        val exerciseMap = createExerciseMap(findExercisesByTrainId(user))
        val chatId = callbackQuery.message.chatId
        if(exerciseMap.isNullOrEmpty())
            throw HandlerException(ExceptionType.NOT_FOUND_EXERCISES, chatId, "У вас нет сохраненных упражнений")

        val userId = callbackQuery.from?.id!!
        val msgList = messageService.getMessage(userId)

        val exeMapWithTreatment = exerciseService.collectTreatmentByExercises(exerciseMap, msgList)

        val exercise = callbackQuery.data
        val exerciseInMap = exeMapWithTreatment[exercise]

        if(exerciseInMap == null || exerciseInMap.isEmpty())
            throw HandlerException(ExceptionType.NOT_FOUND_TREATMENT, chatId, "Не найдены подходы для данного упражнения")

        val ordinates = chartService.buildXYAxis(exeMapWithTreatment[exercise]!!)

        val urlToChart = quickChartService.createChart(ordinates.first, ordinates.second, CHART_NAME)

        return SendMessage()
                .setChatId(chatId)
                .setText(urlToChart)
    }

    private fun findExercisesByTrainId(user: UserEntity) : Set<String> {
        val exercises =  user.exercisesList?.flatMap { e -> e.exercises }?.toSet()
        return exercises ?: emptySet()
    }

    private fun createExerciseMap(exercises: Set<String>) : Map<String, MutableList<String>> {
        return exercises.associateWith { mutableListOf<String>() }
    }

    override fun getStateType(): StateType {
        return StateType.SHOWING_EXE
    }

    companion object {
        const val CHART_NAME  = "СТАТИСТИКА"
        const val EXERCISE_LIST  = "Список упражнений"
    }
}