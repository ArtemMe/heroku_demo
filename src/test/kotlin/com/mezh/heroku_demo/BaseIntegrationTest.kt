package com.mezh.heroku_demo

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [MongoInitializer::class],
        classes = [
            HerokuDemoApplication::class
        ])
class BaseIntegrationTest {

}