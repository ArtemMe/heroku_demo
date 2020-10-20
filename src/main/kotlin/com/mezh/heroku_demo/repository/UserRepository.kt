package com.mezh.heroku_demo.repository;

import com.mezh.heroku_demo.entity.MessageDto;
import com.mezh.heroku_demo.entity.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserEntity, String> {

}