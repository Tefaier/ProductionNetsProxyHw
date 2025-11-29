package com.prod.nets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@Import(
    RestTemplateConfiguration::class,
)
open class ProductionNetsProxyApplication

fun main(args: Array<String>) {
    runApplication<ProductionNetsProxyApplication>(*args)
}
