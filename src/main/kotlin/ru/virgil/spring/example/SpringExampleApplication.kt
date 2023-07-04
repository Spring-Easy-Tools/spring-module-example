package ru.virgil.spring.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

const val basePackage = "ru.virgil.spring"

@SpringBootApplication(scanBasePackages = [basePackage])
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = [basePackage])
class SpringExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringExampleApplication>(*args)
}
