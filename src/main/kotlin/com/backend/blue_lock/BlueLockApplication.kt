package com.backend.blue_lock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
	scanBasePackages = [
		"com.backend.blue_lock",
		"org.jetbrains.exposed.spring",
	],
)
class BlueLockApplication

fun main(args: Array<String>) {
	runApplication<BlueLockApplication>(*args)
}
