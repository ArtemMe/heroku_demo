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
class MainMenuHandler (
        val mainMenuService: MainMenuService,
        val userService: UserService
): CommandHadler {
    override fun handle(context: CommandContext): SendMessage {
        userService.updateState(context.user, UserState(StateType.MAIN_MENU, getType().name, null))
        return mainMenuService.getMenu(context.message.chatId, MAIN_MENU_NAME)
    }

    override fun getType(): Command {
        return Command.MENU
    }

    override fun getStateType(): StateType {
        return StateType.MAIN_MENU
    }

    companion object {
        const val MAIN_MENU_NAME = "main menu"
    }
}