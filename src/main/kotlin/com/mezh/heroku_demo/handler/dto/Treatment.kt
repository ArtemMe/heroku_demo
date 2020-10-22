package com.mezh.heroku_demo.handler.dto

import java.time.LocalDateTime

data class Treatment(
        val number: Int,
        val amount: Int,
        val weight: Int?,
        val dateTime: LocalDateTime
)