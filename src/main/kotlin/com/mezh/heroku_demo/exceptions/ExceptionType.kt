package com.mezh.heroku_demo.exceptions

enum class ExceptionType(
        val desc: String
) {
        EMPTY_INPUT("Пустое сообщение!"),
        NOT_FOUND_EXERCISES("Пустое сообщение!"),
        NOT_FOUND_TREATMENT("Пустое сообщение!"),
        EMPTY_CALLBACK("Пустое сообщение!"),
}
