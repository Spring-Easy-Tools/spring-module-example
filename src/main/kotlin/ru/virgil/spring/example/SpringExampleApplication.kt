package ru.virgil.spring.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE

@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
@EnableJpaAuditing
@EnableScheduling
@ConfigurationPropertiesScan(BASE_PACKAGE)
class SpringExampleApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SpringExampleApplication>(*args)
        }
    }
}
