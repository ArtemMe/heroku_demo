    package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.MessageDto
import com.mezh.heroku_demo.entity.TrainingComplexEntity
import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.entity.UserState
import com.mezh.heroku_demo.handler.CommandHadler
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.repository.MessageRepository
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.*

@Service
class DispatcherService(
        private val messageRepository: MessageRepository,
        private val commandHandlers: List<CommandHadler>,
        private val userService: UserService
) {
    fun saveMessage(message: Message?) {
        messageRepository.save(
                MessageDto(
                        id = message?.messageId?.toInt().toString(),
                        message = message?.text,
                        userId = getUserId(message),
                        dateTime = message?.date?.toInt()
                )
        )
    }

    fun findLastMessage(): String? {
        val messageList = messageRepository.findAll()
        if (messageList.isNotEmpty()) {
            return messageList.last().message
        }
        return "not found last message"
    }

    fun getUserId(message: Message?): Int? {
        return message?.from?.id
    }

    fun createResponse(message: Message?): SendMessage {
        if(message == null) return SendMessage()

        this.saveMessage(message)
        val messageText = message.text
        val user = createUserIfEmpty(message)
        val state =user.currentState

        if (messageText.toString().trim() == Command.START.desc) {
            return getHandler(Command.START).handle(CommandContext(message, user))
        } else if (messageText.toString().contains(Command.ADD_EXERCISES.desc, true)) {
            return getHandler(Command.ADD_EXERCISES).handle(CommandContext(message, user))
        } else if (messageText.toString().contains(Command.STATISTIC.desc, true)) {
            return getHandler(Command.STATISTIC).handle(CommandContext(message, user))
        } else if (messageText.toString().contains(Command.ADD_COMPLEX.desc, true) || checkState(state, Command.ADD_COMPLEX)) {
            return getHandler(Command.ADD_COMPLEX).handle(CommandContext(message, user))
        } else {
            return replyLastMessage(message)
        }
    }


    fun checkState(state: UserState?, command: Command) : Boolean {
        if(state != null) {
            return state.commandName == command.name
        }
        return false
    }

    fun createUserIfEmpty(message: Message?) : UserEntity {
        val userOpt = userService.findUserById(message?.from?.id!!)
        if (userOpt.isEmpty) {
            val newUser = buildUserEntity(message.from?.id!!.toString())
            return userService.save(newUser)
        }

        return userOpt.get()
    }

    private fun buildUserEntity(id: String): UserEntity {

        return UserEntity(
                userId = id,
                exercisesList = null,
                currentState = null
        )
    }

    fun replyLastMessage(message: Message?): SendMessage {
//        val sendMessage = SendMessage()
//        sendMessage.text = this.findLastMessage()
//        sendMessage.chatId = message?.chatId.toString()
//        return sendMessage
        return SendMessage()
    }

    fun getHandler(command: Command) : CommandHadler {
        return commandHandlers
                .first { handler -> handler.getType() == command }
    }

}