package ru.virgil.spring.example.system

import tools.jackson.dataformat.yaml.YAMLMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class YamlMapperProvider {

    @Bean
    fun provideYamlMapper(): YAMLMapper = YAMLMapper.builder().build()
}
