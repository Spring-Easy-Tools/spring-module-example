package ru.virgil.spring.example.box

import org.springframework.stereotype.Repository
import ru.virgil.spring.example.mock.ByOwner
import ru.virgil.spring.example.mock.MockerRepository
import java.util.*

@Repository
interface BoxMockerRepository : MockerRepository<Box, UUID>, ByOwner<Box>
