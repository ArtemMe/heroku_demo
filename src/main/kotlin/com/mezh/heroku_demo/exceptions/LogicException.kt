package com.mezh.heroku_demo.exceptions

abstract class LogicException(message: String?) : Throwable(message) {
    abstract fun getType() : ExceptionType
    abstract fun chatId() : Long
}