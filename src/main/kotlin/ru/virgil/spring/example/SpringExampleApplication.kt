package ru.virgil.spring.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import ru.virgil.spring.tools.toolsBasePackage

@SpringBootApplication(scanBasePackages = [toolsBasePackage])
@EnableJpaAuditing
@EnableScheduling
@ConfigurationPropertiesScan(toolsBasePackage)
class SpringExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringExampleApplication>(*args)
}
