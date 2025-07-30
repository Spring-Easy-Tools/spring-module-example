package ru.virgil.spring.example.image

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.virgil.spring.tools.file.type.FileTypeConfig

@ConfigurationProperties(prefix = "image.service")
data class ImageServiceProperties(
    @Deprecated("Перейти на использвоание экстеншенов")
    val allowedMimeTypeRegexes: List<Regex> = listOf(),
    override val allowedExtensions: List<String> = listOf(),
) : FileTypeConfig
