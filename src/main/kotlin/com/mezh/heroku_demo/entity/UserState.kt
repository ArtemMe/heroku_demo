package com.mezh.heroku_demo.entity

data class UserState (
        val type: StateType,
        val commandName: String,
        val data: StateData
)
