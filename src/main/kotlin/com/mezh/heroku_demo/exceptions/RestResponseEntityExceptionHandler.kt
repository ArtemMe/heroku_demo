package com.mezh.heroku_demo.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [LogicException::class, Exception::class])
    protected fun handleConflict(
            ex: RuntimeException?, request: WebRequest?): ResponseEntity<Any> {

        val logicException: LogicException = ex as LogicException
        val bodyOfResponse = SendMessage()
                .setChatId(logicException.chatId())
                .setText(logicException.message)

        return handleExceptionInternal(ex, bodyOfResponse,
                HttpHeaders(), HttpStatus.OK, request!!)
    }
}