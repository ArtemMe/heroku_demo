package com.mezh.heroku_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer

@SpringBootApplication
class HerokuDemoApplication

fun main(args: Array<String>) {
    ApiContextInitializer.init()
	runApplication<HerokuDemoApplication>(*args)
}
