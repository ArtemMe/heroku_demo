package com.mezh.heroku_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HerokuDemoApplication

fun main(args: Array<String>) {
	runApplication<HerokuDemoApplication>(*args)
}
