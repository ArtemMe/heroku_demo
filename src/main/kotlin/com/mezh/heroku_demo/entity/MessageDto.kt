package com.mezh.heroku_demo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class MessageDto(
        @Id
        var id: String,
        var userId: Int? = null,
        var message: String? = null,
        var dateTime: Int? = null
)