package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.handler.dto.CommandContext
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.*

@Service
class MainMenuHandler : CommandHadler {
    fun getMainMenuMessage(chatId: Long, textMessage: String): SendMessage {
        val replyKeyboardMarkup = mainMenuKeyboard
        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup)
    }

    private val mainMenuKeyboard: ReplyKeyboardMarkup
        private get() {
            val replyKeyboardMarkup = ReplyKeyboardMarkup()
            replyKeyboardMarkup.selective = true
            replyKeyboardMarkup.resizeKeyboard = true
            replyKeyboardMarkup.oneTimeKeyboard = false
            val keyboard: MutableList<KeyboardRow> = ArrayList()
            val row1 = KeyboardRow()
            val row2 = KeyboardRow()
            val row3 = KeyboardRow()
            val row4 = KeyboardRow()
            row1.add(KeyboardButton(Command.ADD_EXERCISES.menuName))
            row2.add(KeyboardButton(Command.STATISTIC.menuName))
            row4.add(KeyboardButton(Command.HELP.menuName))
            keyboard.add(row1)
            keyboard.add(row2)
            keyboard.add(row3)
            keyboard.add(row4)
            replyKeyboardMarkup.keyboard = keyboard
            return replyKeyboardMarkup
        }

    private fun createMessageWithKeyboard(chatId: Long,
                                          textMessage: String,
                                          replyKeyboardMarkup: ReplyKeyboardMarkup?): SendMessage {
        val sendMessage = SendMessage()
        sendMessage.enableMarkdown(true)
        sendMessage.setChatId(chatId)
        sendMessage.text = textMessage
        if (replyKeyboardMarkup != null) {
            sendMessage.replyMarkup = replyKeyboardMarkup
        }
        return sendMessage
    }

    override fun handle(context: CommandContext): SendMessage {
        return getMainMenuMessage(context.message.chatId, "main menu")
    }

    override fun getType(): Command {
        return Command.MENU
    }
}