package com.mezh.heroku_demo.handler.dialogs

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.StateType
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.entity.UserState
import com.mezh.heroku_demo.exceptions.ExceptionType
import com.mezh.heroku_demo.exceptions.HandlerException
import com.mezh.heroku_demo.handler.CommandHadler
import com.mezh.heroku_demo.handler.DialogHandler
import com.mezh.heroku_demo.handler.DialogHandlerFactory
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.MainMenuService
import com.mezh.heroku_demo.services.UserService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class InputExercisesHandler(
        private val userService: UserService,
        private val mainMenuService: MainMenuService
) : DialogHandler {

    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context.message, context.user)
    }

    private fun handleInternal(message: Message, user: UserEntity): SendMessage {

        val prevState = user.currentState!!.type

        if(prevState == StateType.INPUT_EXE) {
            val sendMessage = handleInputExercises(message, user)
            return sendMessage
        }

        return SendMessage()
                .setChatId(message.chatId)
                .setText("Неизвестное состояние...")
    }

    private fun handleInputExercises(msg: Message, user: UserEntity): SendMessage {
        val messageText = msg.text
                ?: throw HandlerException(ExceptionType.EMPTY_INPUT, msg.chatId, EMPTY_EXERCISES)

        val exercises = getExercisesList(messageText)

        if(exercises.isEmpty())
            throw throw HandlerException(ExceptionType.EMPTY_INPUT, msg.chatId, EMPTY_EXERCISES)

        val complex = getComplexOrCreateDefault(user)
        val newUser = buildUserEntityCleanState(user.userId, TRAIN_COMPLEX_DEFAULT, complex.exercises.union(exercises))

        userService.save(newUser)

        val formattedExe = getFormattedString(exercises)

        return createSimpleResponse(msg.chatId,"Добавлены упражнения:\n $formattedExe")
    }

    private fun getComplexOrCreateDefault(user: UserEntity) : TrainingComplexEntity {
        val defaultComplex = TrainingComplexEntity(TRAIN_COMPLEX_DEFAULT, mutableSetOf())

        if(user.exercisesList == null) return defaultComplex

        return user.exercisesList?.find { c -> c.name == TRAIN_COMPLEX_DEFAULT } ?: defaultComplex
    }

    private fun createSimpleResponse(chatId: Long, text: String) : SendMessage {
        return mainMenuService.getMenu(chatId, text)
    }

    private fun getExercisesList(text: String): Set<String> {
        return text
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

    override fun getStateType(): StateType {
        return StateType.INPUT_EXE
    }

    private fun buildUserEntityCleanState(id: String, trainName: String, exercises: Set<String>): UserEntity {

        return UserEntity(
                userId = id,
                exercisesList = setOf(TrainingComplexEntity(trainName, exercises)),
                currentState = null
        )
    }

    companion object {
        const val EMPTY_EXERCISES = "Вы не ввели упражнения :("
        const val TRAIN_COMPLEX_DEFAULT = "default"
    }
}