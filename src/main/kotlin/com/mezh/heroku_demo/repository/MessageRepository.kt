package com.mezh.heroku_demo.repository;

import com.mezh.heroku_demo.dto.MessageDto;
import org.springframework.data.mongodb.repository.MongoRepository;

interface MessageRepository : MongoRepository<MessageDto, String> {

}