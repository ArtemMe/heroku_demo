package com.mezh.heroku_demo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("User")
data class UserEntity(
        @Id
        var userId: String,
        var exercisesList: Set<TrainingComplexEntity>
)