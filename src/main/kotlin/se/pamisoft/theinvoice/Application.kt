package se.pamisoft.theinvoice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// TODO use kotlin json https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin-multiplatform-serialization
// TODO populate database with test data in development
@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
