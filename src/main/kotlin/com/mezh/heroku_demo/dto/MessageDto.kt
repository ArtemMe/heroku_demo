package com.mezh.heroku_demo.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class MessageDto(
        @Id
        var id: Int? = 0,
        var userId: String? = null,
        var message: String? = null,
        var dateTime: Int? = null
)