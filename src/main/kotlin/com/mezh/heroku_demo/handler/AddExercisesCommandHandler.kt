package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.UserService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class AddExercisesCommandHandler(
        private val userService: UserService
) : CommandHadler {
    val TRAIN_ID = "Комплекс 1"

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message)
    }

    private fun handleInternal(message: Message?): SendMessage {
        val messageText = message?.text
        val exercises = getExercisesList(messageText!!)

        val userOpt = userService.findUserById(message.from?.id!!)

        if (userOpt.isPresent) {
            val user = userOpt.get()
            val complex = user.exercisesList.find { c -> c.name == TRAIN_ID }
            val newUser = buildUserEntity(user.userId, TRAIN_ID, complex?.exercises?.union(exercises) as Set<String>)
            userService.save(newUser)
        } else {
            val newUser = buildUserEntity(message.from?.id!!.toString(), TRAIN_ID, exercises)
            userService.save(newUser)
        }

        return SendMessage()
                .setChatId(message.chatId)
                .setText(getFormattedString(exercises))
    }

    private fun getExercisesList(text: String): Set<String> {
        return text.subSequence(Command.ADD_EXERCISES.name.length+1, text.length)
                .split(",")
                .map { exercise -> exercise.trim() }
                .toSet()
    }

    private fun getFormattedString(texts: Set<String>): String {
        val builder = StringBuilder()

        for (s in texts) {
            builder.append(s)
            builder.append("\n")
        }

        return builder.toString()
    }

    override fun getType(): Command {
        return Command.ADD_EXERCISES
    }

    private fun buildUserEntity(id: String, trainName: String, exercises: Set<String>): UserEntity {

        return UserEntity(
                userId = id,
                exercisesList = setOf(TrainingComplexEntity(trainName, exercises))
        )
    }
}