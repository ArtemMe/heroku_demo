package com.mezh.heroku_demo.services

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.util.*

@Service
class ButtonService {
    fun createButtons() : InlineKeyboardMarkup {
        val inlineKeyboardButton = InlineKeyboardButton()
        inlineKeyboardButton.text = "Тык"
        inlineKeyboardButton.callbackData = "Button \"Тык\" has been pressed"

        val keyboardButtonsRow1: List<InlineKeyboardButton> = listOf(inlineKeyboardButton)

        val inlineKeyboardButton2 = InlineKeyboardButton()
        inlineKeyboardButton2.text = "Тык2"
        inlineKeyboardButton2.callbackData = "Button \"Тык2\" has been pressed"

        val keyboardButtonsRow2: List<InlineKeyboardButton> = listOf(inlineKeyboardButton2)


        val rowList: MutableList<List<InlineKeyboardButton>> = ArrayList()
        rowList.add(keyboardButtonsRow1)
        rowList.add(keyboardButtonsRow2)

        val inlineKeyboardMarkup = InlineKeyboardMarkup()

        inlineKeyboardMarkup.keyboard = rowList;

        return inlineKeyboardMarkup
    }
}