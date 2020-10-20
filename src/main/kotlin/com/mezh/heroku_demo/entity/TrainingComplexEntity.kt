package com.mezh.heroku_demo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("TrainingComplex")
data class TrainingComplexEntity (
        @Id
        val name: String,
        val exercises: Set<String>
)