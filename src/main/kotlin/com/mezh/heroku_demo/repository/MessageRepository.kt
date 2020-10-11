package com.mezh.heroku_demo.repository;

import com.mezh.heroku_demo.dto.MessageDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : MongoRepository<MessageDto, String> {

}