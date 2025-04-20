package ru.virgil.spring.example.box

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BoxGeneratorRepository : CrudRepository<Box, UUID>
