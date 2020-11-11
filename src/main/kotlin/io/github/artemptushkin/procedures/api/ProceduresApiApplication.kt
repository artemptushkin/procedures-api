package io.github.artemptushkin.procedures.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProceduresApiApplication

fun main(args: Array<String>) {
	runApplication<ProceduresApiApplication>(*args)
}
