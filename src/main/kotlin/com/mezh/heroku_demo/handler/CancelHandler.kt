package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.dto.Command
import com.mezh.heroku_demo.entity.StateType
import com.mezh.heroku_demo.entity.UserState
import com.mezh.heroku_demo.handler.dto.CommandContext
import com.mezh.heroku_demo.services.MainMenuService
import com.mezh.heroku_demo.services.UserService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
class CancelHandler (
        val mainMenuService: MainMenuService,
        val userService: UserService
): CommandHadler {
    override fun handle(context: CommandContext): SendMessage {
        userService.updateState(context.user, null)
        return mainMenuService.getMenu(context.message.chatId, MAIN_MENU_NAME)
    }

    override fun getType(): Command {
        return Command.CANCEL
    }

    override fun getStateType(): StateType {
        return StateType.CANCEL
    }

    companion object {
        const val MAIN_MENU_NAME = "Главное меню:"
    }
}