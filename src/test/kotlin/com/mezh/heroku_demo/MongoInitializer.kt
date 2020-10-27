package com.mezh.heroku_demo

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.Wait

class MongoInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val mongoContainer = MongoDBContainer(MONGO_IMAGE)
            .waitingFor(Wait.defaultWaitStrategy())
            .withExposedPorts(MONGO_PORT)
            .withCommand(BIND_IP)

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        mongoContainer.start()
        TestPropertyValues.of(
                "spring.data.mongodb.host=" + mongoContainer.containerIpAddress,
                "spring.data.mongodb.port=" + mongoContainer.getMappedPort(MONGO_PORT),
                "spring.data.mongodb.database=test")
                .applyTo(applicationContext.environment)
    }

    companion object {
        private const val MONGO_PORT = 27017
        private const val MONGO_IMAGE = "mongo:4.0.10"
        private const val BIND_IP = "--bind_ip_all"
    }
}