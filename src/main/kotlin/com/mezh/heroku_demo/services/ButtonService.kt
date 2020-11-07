package com.mezh.heroku_demo.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.handler.dto.CallbackExerciseDto
import org.springframework.stereotype.Service
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

    fun createListButtons(listButtonNameCallback: Set<String>) : InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        inlineKeyboardMarkup.keyboard = createListButtonsFromSet(listButtonNameCallback);
        return inlineKeyboardMarkup
    }

    private fun createButton(buttonName: String, callBack: String) : InlineKeyboardButton {
        val inlineKeyboardButton = InlineKeyboardButton()
        inlineKeyboardButton.text = buttonName
        inlineKeyboardButton.callbackData = convertToJson(Command.STATISTIC.name, callBack)

        return inlineKeyboardButton
    }

    private fun createListButtonsFromSet(listButtonNameCallback: Set<String>) : List<List<InlineKeyboardButton>> {
        return listButtonNameCallback
                .map { listOf(createButton(it, it))}
                .toList()
    }

    private fun convertToJson(handlerType: String, value: String) : String {
        return "$handlerType|$value"
//        val objectMapper = ObjectMapper()
//        return objectMapper.writeValueAsString(CallbackExerciseDto(handlerType, value))
    }
}