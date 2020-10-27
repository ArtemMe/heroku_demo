package com.mezh.heroku_demo

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(initializers = [MongoInitializer::class],
        classes = [
            HerokuDemoApplication::class
        ])
class BaseIntegrationTest {

}