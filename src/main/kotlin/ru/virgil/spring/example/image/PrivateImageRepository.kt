package ru.virgil.spring.example.image

import org.springframework.stereotype.Repository
import ru.virgil.spring.tools.file.PrivateFileRepository

@Repository
interface PrivateImageRepository : PrivateFileRepository<PrivateImageFile>
