package ru.virgil.spring.example.image

import net.datafaker.Faker
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.file.FileProperties
import ru.virgil.spring.tools.file.FileService

@Service
class ImageMockService(
    imageService: FileService<PrivateImageFile>,
    imageProperties: FileProperties,
    faker: Faker,
) : ru.virgil.spring.tools.file.mock.ImageMockService<PrivateImageFile>(
    imageService,
    imageProperties,
    faker,
)
