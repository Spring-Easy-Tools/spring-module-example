package ru.virgil.spring.example.image

import net.datafaker.Faker
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.image.ImageMockService
import ru.virgil.spring.tools.image.ImageProperties
import ru.virgil.spring.tools.image.ImageService

// TODO: Нужно ли?
@Service
class ImageMockService(
    imageService: ImageService<PrivateImageFile>,
    imageProperties: ImageProperties,
    faker: Faker,
) : ImageMockService<PrivateImageFile>(
    imageService,
    imageProperties,
    faker,
)
