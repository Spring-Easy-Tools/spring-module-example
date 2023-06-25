package ru.virgil.spring.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import ru.virgil.spring.tools.util.LocalDateTimeUtil.databaseTruncate
import java.time.LocalDateTime

@SpringBootApplication(scanBasePackages = ["ru.virgil.spring.tools"])
@EnableJpaAuditing
@ConfigurationPropertiesScan
class SpringExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringExampleApplication>(*args)
}

val localDateTime = LocalDateTime.now().databaseTruncate()
