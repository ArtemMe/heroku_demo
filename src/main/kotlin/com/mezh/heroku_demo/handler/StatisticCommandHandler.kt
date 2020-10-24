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
        private val userService: UserService,
        private val messageService: MessageService,
        private val quickChartService: QuickChartService,
        private val chartService: ChartService,
        private val exerciseService: ExerciseService
) : CommandHadler {
    val TRAIN_ID = "Комплекс 1"

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message)
    }

    private fun handleInternal(message: Message?): SendMessage {
        val userId = message?.from?.id!!
        val user = userService.findUserById(userId)

        if(!user.isPresent) {
            return SendMessage();
        }

        val exeMap = createExerMap(findExercisesByTrainId(user.get()))
        val msgList = messageService.getMessage(userId)

        val exeMapWithTreatment = exerciseService.collectTreatmentByExercises(exeMap, msgList)
        val ordinats = chartService.buildXYAxis(exeMapWithTreatment.get("жим лежа")!!)

        val urlToChart = quickChartService.createChart(ordinats.first, ordinats.second, CHART_NAME)

        return SendMessage()
                .setChatId(message.chatId)
                .setText(urlToChart)
    }

    private fun calculateAvg(exeMap: Map<String, MutableList<String>>) : Map<String, Int> {
        return exeMap.entries.associateBy(keySelector = {it.key}, valueTransform = {calculateTotalSum(it.value)} )
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

    private fun createExerMap(exercises: Set<String>) : Map<String, MutableList<String>> {
        return exercises.associateWith { mutableListOf<String>() }
    }

    companion object {
        val CHART_NAME  = "СТАТИСТИКА"
    }
}