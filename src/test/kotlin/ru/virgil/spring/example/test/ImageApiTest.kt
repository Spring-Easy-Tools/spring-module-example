package ru.virgil.spring.example.test

import io.kotest.matchers.ints.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import ru.virgil.spring.example.image.ImageMockService
import ru.virgil.spring.example.image.ImageService
import ru.virgil.spring.example.image.PrivateImageFileDto
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.image.FileTypeService
import ru.virgil.spring.tools.testing.MockMvcExtensions.Companion.fromJson
import tools.jackson.databind.ObjectMapper
import java.util.*

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockedUser
class ImageApiTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val imageMockService: ImageMockService,
    val imageService: ImageService,
    val fileTypeService: FileTypeService,
) {

    private val imageMimeTypePattern = "image/"

    @Test
    fun postPrivateImage() {
        val privateImageFileDto: PrivateImageFileDto = mockMvc.multipart("/image/private") {
            with(testSecurityContext())
            with(csrf())
            file(imageMockService.mockAsMultipart())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        privateImageFileDto.shouldNotBeNull()
    }

    @Test
    fun getPrivateImage() {
        val privateImageFileDto: PrivateImageFileDto = mockMvc.multipart("/image/private") {
            with(testSecurityContext())
            with(csrf())
            file(imageMockService.mockAsMultipart())
        }.andExpect {
            status { isOk() }
        }.andReturn().fromJson()
        privateImageFileDto.shouldNotBeNull()
        val byteArray: ByteArray = mockMvc.get("/image/private/${privateImageFileDto.uuid}") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().response.contentAsByteArray
        fileTypeService.getImageMimeType(byteArray) shouldContain imageMimeTypePattern
        byteArray.size.shouldNotBeZero()
    }

    @Test
    fun getProtectedImage() {
        val byteArray: ByteArray = mockMvc.get("/image/protected/image.jpg") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().response.contentAsByteArray
        fileTypeService.getImageMimeType(byteArray) shouldContain imageMimeTypePattern
        byteArray.size.shouldNotBeZero()
    }

    @Test
    fun getPublicImage() {
        val byteArray: ByteArray = mockMvc.get("/image/public/image.jpg") {
            with(testSecurityContext())
        }.andExpect {
            status { isOk() }
        }.andReturn().response.contentAsByteArray
        fileTypeService.getImageMimeType(byteArray) shouldContain imageMimeTypePattern
        byteArray.size.shouldNotBeZero()
    }

    @Test
    fun getNotExisting() {
        mockMvc.get("/image/public/not_existing.jpg") {
            with(testSecurityContext())
        }.andExpect {
            status { isNotFound() }
        }
        mockMvc.get("/image/protected/not_existing.jpg") {
            with(testSecurityContext())
        }.andExpect {
            status { isNotFound() }
        }
        mockMvc.get("/protected/${UUID.randomUUID()}") {
            with(testSecurityContext())
        }.andExpect {
            status { isNotFound() }
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
