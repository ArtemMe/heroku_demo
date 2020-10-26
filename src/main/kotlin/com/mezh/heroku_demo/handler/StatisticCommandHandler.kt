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
    val TRAIN_ID = "Комплекс 1"

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message, context.user)
    }

    private fun handleInternal(message: Message, user: UserEntity): SendMessage {
        val userId = message.from?.id!!

        val exercise = parseExe(message.text)
                ?: return createErrorMessage("Введите упражнение", message.chatId)

        val exerciseMap = createExerciseMap(findExercisesByTrainId(user))
        val msgList = messageService.getMessage(userId)

        val exeMapWithTreatment = exerciseService.collectTreatmentByExercises(exerciseMap, msgList)
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

    private fun calculateAvg(exeMap: Map<String, MutableList<String>>) : Map<String, Int> {
        return exeMap.entries.associateBy(keySelector = {it.key}, valueTransform = {calculateTotalSum(it.value)} )
    }

    private fun parseExe(inputMsg: String?) : String? {
        return inputMsg?.substring(getType().desc.length + 1)?.trim()
    }

    private fun calculateTotalSum(values: MutableList<String>): Int {
        return values.map { v -> getAmount(v) }.sum()
    }

    private fun getAmount(str: String) : Int {
        val strArr = str.split(":")

        if (strArr.size == 2) {
            return (strArr[1]).toInt()
        }

        if (strArr.size == 3) {
            return (strArr[2]).toInt()
        }

        return 0;
    }

    private fun findExercisesByTrainId(user: UserEntity) : Set<String> {
        val exercises =  user.exercisesList?.find { e -> e.name == TRAIN_ID }
        return exercises?.exercises ?: emptySet()
    }

    override fun getType(): Command {
        return Command.STATISTIC
    }

    private fun createExerciseMap(exercises: Set<String>) : Map<String, MutableList<String>> {
        return exercises.associateWith { mutableListOf<String>() }
    }

    companion object {
        val CHART_NAME  = "СТАТИСТИКА"
    }
}