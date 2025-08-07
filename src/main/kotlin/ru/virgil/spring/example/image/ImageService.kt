package ru.virgil.spring.example.image

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import ru.virgil.spring.tools.file.FileProperties
import ru.virgil.spring.tools.file.type.FileTypeService
import ru.virgil.spring.tools.security.Security.getCreator
import java.nio.file.Path
import java.util.*

@Service
class ImageService(
    resourceLoader: ResourceLoader,
    privateImageRepository: PrivateImageRepository,
    fileTypeService: FileTypeService,
    val fileProperties: FileProperties,
) : ru.virgil.spring.tools.file.FileService<PrivateImageFile>(
    resourceLoader,
    privateImageRepository,
    fileTypeService,
    fileProperties,
) {

    override fun createPrivateFile(
        uuid: UUID,
        creator: String,
        filePath: Path,
    ): PrivateImageFile {
        val privateImageFile = PrivateImageFile(filePath)
        privateImageFile.uuid = uuid
        return privateImageFile
    }

    fun savePrivate(
        content: ByteArray,
        name: String = fileProperties.defaultFileName,
        creator: String = getCreator(),
    ): PrivateImageFile {
        return savePrivate(content, fileProperties.allowedExtensions, name, creator)
    }
}
