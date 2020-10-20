package com.mezh.heroku_demo.services

import com.mezh.heroku_demo.entity.UserEntity
import com.mezh.heroku_demo.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService (
        val userRepository: UserRepository
) {
    fun findUserById(id: Int) : Optional<UserEntity> {
        return userRepository.findById(id.toString())
    }

    fun save(user: UserEntity): UserEntity {
        return userRepository.save(user)
    }
}