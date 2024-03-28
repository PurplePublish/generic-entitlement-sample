package com.sprylab.purple.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GenericEntitlementSampleApplication

fun main(args: Array<String>) {
	runApplication<GenericEntitlementSampleApplication>(*args)
}
