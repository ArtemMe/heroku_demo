package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.StateType
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.entity.UserState
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.handler.dto.ComplexDto
import com.mezh.heroku_demo.services.UserService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.*

@Service
class AddComplexCommandHandler(
        private val userService: UserService
) : CommandHadler {

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message, context.user)
    }

    private fun handleInternal(message: Message?, user: UserEntity): SendMessage {
        val messageText = message?.text
        val userState: UserState?

        if (user.currentState == null) {
            val complexDto = ComplexDto()
            complexDto.exeList = getExercisesList(messageText!!)
            userState = UserState(StateType.INPUT_EXE, getType().name, complexDto)
            val newUser = buildUserEntity(user, userState)
            userService.save(newUser)

            return SendMessage()
                    .setChatId(message.chatId)
                    .setText("Введите название комплекса...")
        }

        if (complexExist(user, messageText)) {
            return SendMessage()
                    .setChatId(message?.chatId)
                    .setText("Такое имя комплекса уже есть в базе")
        }

        val complex = user.currentState!!.data as ComplexDto

        val newUser = buildUserEntity(user.userId, messageText!!, user.exercisesList, complex.exeList!!)
        userService.save(newUser)

        val formattedExe = getFormattedString(complex.exeList!!)

        return SendMessage()
                .setChatId(message.chatId)
                .setText("Добавлен комплекс ${messageText} c такими упражнениями:\n ${formattedExe}")
    }

    private fun complexExist(user: UserEntity, complexName: String?) : Boolean {
        val complexDuplicate = user.exercisesList?.find { c -> c.name == complexName }

        return complexDuplicate != null
    }

    private fun getExercisesList(text: String): Set<String> {
        return text.subSequence(getType().name.length + 1, text.length)
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
        return Command.ADD_COMPLEX
    }

    override fun getStateType(): StateType {
       return StateType.INPUT_COMPLEX_NAME
    }

    private fun buildUserEntity(
            id: String, trainName: String, currentComplexes: Set<TrainingComplexEntity>?, exercises: Set<String>): UserEntity {

        val exercisesList: Set<TrainingComplexEntity>

        if(currentComplexes != null) {
            exercisesList = currentComplexes.union(setOf(TrainingComplexEntity(trainName, exercises)))
        } else {
            exercisesList = setOf(TrainingComplexEntity(trainName, exercises))
        }
        return UserEntity(
                userId = id,
                exercisesList = exercisesList,
                currentState = null
        )
    }

    private fun buildUserEntity(user: UserEntity, currentState: UserState): UserEntity {

        return UserEntity(
                userId = user.userId,
                exercisesList = user.exercisesList,
                currentState = currentState
        )
    }
}