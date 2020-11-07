package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.dto.Command
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import java.util.ArrayList

@Service
class AddExerciseMenuService(
        val menuService: MenuService
) {
    fun getMenu(chatId: Long, textMessage: String): SendMessage {
        val replyKeyboardMarkup = mainMenuKeyboard
        return menuService.createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup)
    }

    private val mainMenuKeyboard: ReplyKeyboardMarkup
        private get() {
            val replyKeyboardMarkup = ReplyKeyboardMarkup()
            replyKeyboardMarkup.selective = true
            replyKeyboardMarkup.resizeKeyboard = true
            replyKeyboardMarkup.oneTimeKeyboard = false
            val keyboard: MutableList<KeyboardRow> = ArrayList()
            val row4 = KeyboardRow()
            row4.add(KeyboardButton(Command.CANCEL.menuName))
            keyboard.add(row4)
            replyKeyboardMarkup.keyboard = keyboard
            return replyKeyboardMarkup
        }
}