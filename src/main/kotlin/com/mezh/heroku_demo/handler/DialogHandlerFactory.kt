package com.mezh.heroku_demo.handler

import com.mezh.heroku_demo.entity.StateType
import org.springframework.stereotype.Service

@Service
class DialogHandlerFactory(
        private val commandHandlers: List<DialogHandler>
) {
    fun getHandler(stateType: StateType) : DialogHandler {
        return commandHandlers
                .first { handler -> handler.getStateType() == stateType }
    }
}