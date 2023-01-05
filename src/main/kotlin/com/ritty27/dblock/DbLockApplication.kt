package com.ritty27.dblock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DbLockApplication

fun main(args: Array<String>) {
	runApplication<DbLockApplication>(*args)
}
