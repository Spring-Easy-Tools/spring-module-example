package ru.virgil.spring.example.image

import kotlin.io.path.name

interface ImageMapper {

    fun PrivateImageFile.toDto(): PrivateImageFileDto =
        PrivateImageFileDto(uuid, createdAt, updatedAt, this.fileLocation.name)
}
