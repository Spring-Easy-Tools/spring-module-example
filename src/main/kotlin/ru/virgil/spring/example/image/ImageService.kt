package ru.virgil.spring.example.image

import org.springframework.core.io.ResourceLoader
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.image.FileTypeService
import ru.virgil.spring.tools.image.ImageService
import java.nio.file.Path
import java.util.*

@Service
class ImageService(
    resourceLoader: ResourceLoader,
    privateImageRepository: PrivateImageRepository,
    fileTypeService: FileTypeService,
) : ImageService<PrivateImageFile>(
    resourceLoader,
    privateImageRepository,
    fileTypeService
) {

    override fun createPrivateImageFile(uuid: UUID, owner: UserDetails, imageFilePath: Path): PrivateImageFile {
        val privateImageFile = PrivateImageFile(imageFilePath)
        privateImageFile.uuid = uuid
        return privateImageFile
    }
}
