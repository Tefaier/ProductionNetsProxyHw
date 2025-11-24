package com.prod.nets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ProductionNetsApplication

fun main(args: Array<String>) {
    runApplication<ProductionNetsApplication>(*args)
}
