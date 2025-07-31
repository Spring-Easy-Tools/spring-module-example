package ru.virgil.spring.example.test

import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.ints.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.virgil.spring.example.image.ImageMockService
import ru.virgil.spring.example.image.ImageService
import ru.virgil.spring.example.image.PrivateImageFileDto
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.file.type.FileTypeService
import ru.virgil.spring.tools.testing.fluent.Fluent
import java.util.*

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageApiTest @Autowired constructor(
    val fluent: Fluent,
    val imageMockService: ImageMockService,
    val imageService: ImageService,
    val fileTypeService: FileTypeService,
) {

    private val imageMimeTypePattern = "image/"

    @WithMockedUser
    @Test
    fun postPrivateImage() {
        val mockMultipartFile = imageMockService.mockAsMultipart()
        val mockFileType = fileTypeService.detect(mockMultipartFile.inputStream)
        val privateImageFileDto: PrivateImageFileDto = fluent.request {
            post { "/image/private" }
            file { mockMultipartFile }
        }
        privateImageFileDto.shouldNotBeNull()
        mockFileType shouldContain imageMimeTypePattern
    }

    @WithMockedUser
    @Test
    fun getPrivateImage() {
        val mockMultipartFile = imageMockService.mockAsMultipart()
        val mockFileType = fileTypeService.detect(mockMultipartFile.inputStream)
        val privateImageFileDto: PrivateImageFileDto = fluent.request {
            post { "/image/private" }
            file { mockMultipartFile }
        }
        privateImageFileDto.shouldNotBeNull()
        val byteArray: ByteArray = fluent.request { get { "/image/private/${privateImageFileDto.uuid}" } }
        val mimeType = fileTypeService.getMimeType(byteArray)
        mimeType.name shouldContain imageMimeTypePattern
        mimeType.name shouldBeEqualIgnoringCase mockFileType
        byteArray.size.shouldNotBeZero()
    }

    @WithMockedUser
    @Test
    fun getProtectedImage() {
        val byteArray: ByteArray = fluent.request { get { "/image/protected/image.jpg" } }
        val mimeType = fileTypeService.getMimeType(byteArray)
        mimeType.name shouldContain imageMimeTypePattern
        byteArray.size.shouldNotBeZero()
    }

    @Test
    fun getPublicImage() {
        val byteArray: ByteArray = fluent.request { get { "/image/public/image.jpg" } }
        val mimeType = fileTypeService.getMimeType(byteArray)
        mimeType.name shouldContain imageMimeTypePattern
        byteArray.size.shouldNotBeZero()
    }

    @WithMockedUser
    @Test
    fun getNotExisting() {
        fluent.request<Any> {
            get { "/image/public/not_existing.jpg" }
            expect { status().isNotFound }
        }
        fluent.request<Any> {
            get { "/image/protected/not_existing.jpg" }
            expect { status().isNotFound }
        }
        fluent.request<Any> {
            get { "/protected/${UUID.randomUUID()}" }
            expect { status().isNotFound }
        }
    }

    /**
     * Сделал удаление после разрушения компонента [ImageMockService], может удалить нужные пользователю картинки,
     * костыль.
     */
    @AfterAll
    fun cleanUp() {
        imageService.cleanFolders()
    }
}
