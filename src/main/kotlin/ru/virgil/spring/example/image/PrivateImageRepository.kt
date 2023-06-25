package ru.virgil.spring.example.image

import org.springframework.stereotype.Repository
import ru.virgil.spring.tools.image.PrivateImageRepositoryInterface

@Repository
interface PrivateImageRepository : PrivateImageRepositoryInterface<PrivateImageFile>
