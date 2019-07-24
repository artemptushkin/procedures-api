package ru.alfabank.testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OcrmProceduresApiApplication

fun main(args: Array<String>) {
	runApplication<OcrmProceduresApiApplication>(*args)
}
