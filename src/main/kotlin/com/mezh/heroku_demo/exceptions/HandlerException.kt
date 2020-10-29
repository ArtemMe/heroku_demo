package com.mezh.heroku_demo.exceptions

class HandlerException(
        val typeEx: ExceptionType,
        val chatId: Long,
        message: String?
) : LogicException(message) {

    override fun getType(): ExceptionType {
        return typeEx
    }

    override fun chatId(): Long {
        return chatId
    }
}