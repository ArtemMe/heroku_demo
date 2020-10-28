package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.*
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class StatisticCommandHandler (
        private val messageService: MessageService,
        private val quickChartService: QuickChartService,
        private val chartService: ChartService,
        private val exerciseService: ExerciseService
) : CommandHadler {
    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message, context.user)
    }

    private fun handleInternal(message: Message, user: UserEntity): SendMessage {
        val userId = message.from?.id!!

        val exercise = parseExercise(message.text)
                ?: return createErrorMessage("Введите упражнение", message.chatId)

        val exerciseMap = createExerciseMap(findExercisesByTrainId(user))

        if(exerciseMap.isNullOrEmpty())
            return createErrorMessage("У вас нет сохраненных упражнений", message.chatId)

        val msgList = messageService.getMessage(userId)

        val exeMapWithTreatment = exerciseService.collectTreatmentByExercises(exerciseMap, msgList)
        val exerciseInMap = exeMapWithTreatment[exercise]

        if(exerciseInMap == null || exerciseInMap.isEmpty())
            return createErrorMessage("Не найдены подходы для данного упражнения", message.chatId)

        val ordinates = chartService.buildXYAxis(exeMapWithTreatment[exercise]!!)

        val urlToChart = quickChartService.createChart(ordinates.first, ordinates.second, CHART_NAME)

        return SendMessage()
                .setChatId(message.chatId)
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

    companion object {
        const val CHART_NAME  = "СТАТИСТИКА"
    }
}