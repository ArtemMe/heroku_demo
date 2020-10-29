package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.StateType
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.entity.UserState
import com.mezh.heroku_demo.exceptions.ExceptionType
import com.mezh.heroku_demo.exceptions.HandlerException
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.*
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class StatisticCommandHandler (
        private val messageService: MessageService,
        private val quickChartService: QuickChartService,
        private val chartService: ChartService,
        private val exerciseService: ExerciseService,
        private val userService: UserService
) : CommandHadler {
    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message, context.user, context.callbackQuery)
    }

    private fun handleInternal(message: Message, user: UserEntity, callbackQuery: CallbackQuery?): SendMessage {

        val state = user.currentState

        if(state == null) {
            userService.updateState(user, UserState(StateType.INPUT_EXE, getType().name, null))
            return createExercisesButtonList(message.chatId, user.exercisesList)
        }

        if(state.type == StateType.INPUT_EXE) {
            val sendMessage = handleInputExercises(callbackQuery, user)
            userService.updateState(user, null)
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

    private fun createErrorMessage(desc: String, msgId: Long) : SendMessage {
        return SendMessage()
                .setChatId(msgId)
                .setText(desc)
    }

    private fun parseExercise(inputMsg: String?) : String? {
        if(inputMsg == null) return null
        if(inputMsg.length <= getType().desc.length) return null

        return inputMsg.substring(getType().desc.length + 1).trim()
    }

    private fun findExercisesByTrainId(user: UserEntity) : Set<String> {
        val exercises =  user.exercisesList?.flatMap { e -> e.exercises }?.toSet()
        return exercises ?: emptySet()
    }

    override fun getType(): Command {
        return Command.STATISTIC
    }

    private fun createExerciseMap(exercises: Set<String>) : Map<String, MutableList<String>> {
        return exercises.associateWith { mutableListOf<String>() }
    }

    private fun createExercisesButtonList(chatId: Long, exercisesSet: Set<TrainingComplexEntity>?) : SendMessage {
        return SendMessage()
                .setChatId(chatId)
                .setText(EXERCISE_LIST)
                .setReplyMarkup(exerciseService.createListButtonsExercises(exercisesSet))
    }

    companion object {
        const val CHART_NAME  = "СТАТИСТИКА"
        const val EXERCISE_LIST  = "Список упражнений"
    }
}