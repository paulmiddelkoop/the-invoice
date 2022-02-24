package se.pamisoft.theinvoice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// TODO remove se.pamisoft.theinvoice directory, see https://kotlinlang.org/docs/coding-conventions.html#directory-structure
@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}