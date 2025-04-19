package ru.virgil.spring.example.image

import org.apache.commons.io.IOUtils
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.virgil.spring.tools.image.FileTypeService
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import ru.virgil.spring.tools.security.cors.GlobalCors
import java.nio.file.Paths
import java.util.*

@GlobalCors
@RestController
@RequestMapping("/image")
class ImageController(
    private val imageService: ImageService,
    private val fileTypeService: FileTypeService,
    // private val securityUserService: SecurityUserService,
) : ImageMapper {

    // val securityUser by lazy { securityUserService.principal }
    // val securityUser by lazy { Mockito.mock(SecurityUser::class.java).username }

    @GetMapping("/public/{imageName}")
    fun getPublic(@PathVariable imageName: String): ResponseEntity<ByteArray> {
        val filePath = Paths.get(imageService.getPublic(imageName).uri)
        val imageBytes = IOUtils.toByteArray(FileSystemResource(filePath).inputStream)
        val imageMime = fileTypeService.getImageMimeType(imageBytes)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes)
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/protected/{imageName}")
    fun getProtected(@PathVariable imageName: String): ResponseEntity<ByteArray> {
        val filePath = Paths.get(imageService.getProtected(imageName).uri)
        val imageBytes = IOUtils.toByteArray(FileSystemResource(filePath).inputStream)
        val imageMime = fileTypeService.getImageMimeType(imageBytes)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes)
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/private/{imageUuid}")
    fun getPrivate(@PathVariable imageUuid: UUID): ResponseEntity<ByteArray> {
        val filePath = Paths.get(imageService.getPrivate(getSimpleCreator(), imageUuid).uri)
        val imageBytes = IOUtils.toByteArray(FileSystemResource(filePath).inputStream)
        val imageMime = fileTypeService.getImageMimeType(imageBytes)
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes)
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("/private")
    fun postPrivate(
        @RequestParam image: MultipartFile,
        @RequestParam(required = false) imageName: String?,
    ): PrivateImageFileDto {
        val privateFileImage =
            imageService.savePrivate(image.bytes, imageName ?: "image-name", getSimpleCreator())
        return privateFileImage.toDto()
    }
}
