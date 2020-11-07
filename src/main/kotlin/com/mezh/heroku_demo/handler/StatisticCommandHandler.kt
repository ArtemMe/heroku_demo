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
        private val exerciseService: ExerciseService,
        private val userService: UserService,
        private val dialogHandlerFactory: DialogHandlerFactory
) : CommandHadler {
    override fun handle(context: CommandContext): SendMessage {
        return handleInternal(context)
    }

    private fun handleInternal(context: CommandContext): SendMessage {
        val user = context.user
        val message = context.message
        val prevState = user.currentState!!.type

        if(prevState == StateType.MAIN_MENU) {
            userService.updateState(user, UserState(StateType.SHOWING_EXE, getType().name, null))
            return createExercisesButtonList(message.chatId, user.exercisesList)
        }

        if(prevState == StateType.SHOWING_EXE) {
            val sendMessage = dialogHandlerFactory.getHandler(prevState).handle(context)
            userService.updateState(user, null)
            return sendMessage
        }

        return SendMessage()
                .setChatId(message.chatId)
                .setText("Неизвестное состояние...")
    }

    override fun getType(): Command {
        return Command.STATISTIC
    }

    override fun getStateType(): StateType {
        TODO("Not yet implemented")
    }

    private fun createExercisesButtonList(chatId: Long, exercisesSet: Set<TrainingComplexEntity>?) : SendMessage {
        return SendMessage()
                .setChatId(chatId)
                .setText(EXERCISE_LIST)
                .setReplyMarkup(exerciseService.createListButtonsExercises(exercisesSet))
    }

    companion object {
        const val EXERCISE_LIST  = "Список упражнений"
    }
}