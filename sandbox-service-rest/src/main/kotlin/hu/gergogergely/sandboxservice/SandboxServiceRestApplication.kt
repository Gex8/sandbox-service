package hu.gergogergely.sandboxservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
class SandboxServiceRestApplication

fun main(args: Array<String>) {
	runApplication<SandboxServiceRestApplication>(*args)
}
