package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.repository.MessageRepository
import com.mezh.heroku_demo.services.UserService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import java.lang.StringBuilder

@Service
class StatisticCommandHandler (
        private val userService: UserService,
        private val messageRepository: MessageRepository
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
        val msgList = getMessage(userId)

        collectTreatmentByExercises(exeMap, msgList)

        return SendMessage()
                .setChatId(message.chatId)
                .setText(createMsg(calculateAvg(exeMap)))
    }

    private fun createMsg(map: Map<String, Int>): String {
        val result = StringBuilder()

        map.entries.forEach { result.append(it.key + ": " + it.value).append("\n") }

        return result.toString()
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
        val exercises =  user.exercisesList.find { e -> e.name == TRAIN_ID }
        return exercises?.exercises ?: emptySet()
    }

    private fun collectTreatmentByExercises(exeMap: Map<String, MutableList<String>>, msgList: List<MessageDto>) {
        var currentExe : String? = null
        var wrongExercise = false

        for(m in msgList) {
            val text = m.message

            if (exeMap.containsKey(text)) {
                currentExe = text!!
                wrongExercise = false
                continue
            } else if (!isTreatmentRecord(text)) {
                wrongExercise = true
                continue
            }

            if (isTreatmentRecord(text) && currentExe != null && !wrongExercise) {
                exeMap.get(currentExe)?.add(text!!)
            }
        }
    }

    override fun getType(): Command {
        return Command.STATISTIC
    }

    private fun getMessage(userId: Int) : List<MessageDto> {
        return messageRepository.findAll()
                .filter { messageDto -> messageDto.userId == userId }
                .sortedBy { messageDto -> messageDto.dateTime }
    }

    private fun isTreatmentRecord(text: String?) : Boolean {
        text ?: return false
        val regex = Regex(pattern = "\\d*:\\d*:\\d*")
        return regex.containsMatchIn(input = text)
    }


    private fun createExerMap(exercises: Set<String>) : Map<String, MutableList<String>> {
        return exercises.associateWith { mutableListOf<String>() }
    }
}