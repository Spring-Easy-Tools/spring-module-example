package ru.virgil.spring.example.system

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class YamlMapperProvider {

    @Bean
    fun provideYamlMapper(): YAMLMapper = YAMLMapper().registerModule(JavaTimeModule()) as YAMLMapper
}