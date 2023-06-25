package ru.virgil.spring.example.mock

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.virgil.spring.example.system.entity.OwnedRepository
import java.util.*

@Repository
interface MockRecordRepository : CrudRepository<MockRecord, UUID>, OwnedRepository<MockRecord>
