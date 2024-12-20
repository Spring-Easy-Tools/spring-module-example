package ru.virgil.spring.example.box

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import java.util.*

@Repository
interface BoxGeneratorRepository : CrudRepository<Box, UUID>, OwnedRepository<Box>
