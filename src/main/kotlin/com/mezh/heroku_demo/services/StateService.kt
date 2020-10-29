package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.entity.UserState
import com.mezh.heroku_demo.repository.UserRepository

class StateService (
        val userRepository: UserRepository
) {
    fun updateState(userId: String, userState: UserState) {

    }
}