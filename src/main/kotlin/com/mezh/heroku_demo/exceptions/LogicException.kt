package com.mezh.heroku_demo.exceptions

import java.lang.RuntimeException

abstract class LogicException(message: String?) : RuntimeException(message) {
    abstract fun getType() : ExceptionType
    abstract fun chatId() : Long
}