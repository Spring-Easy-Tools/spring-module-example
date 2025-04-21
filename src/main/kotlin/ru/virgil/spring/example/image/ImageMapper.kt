package ru.virgil.spring.example.image

import kotlin.io.path.name

interface ImageMapper {

    fun PrivateImageFile.toDto() = PrivateImageFileDto(
        uuid = uuid,
        createdAt = createdAt,
        updatedAt = updatedAt,
        name = this.fileLocation.name
    )
}
