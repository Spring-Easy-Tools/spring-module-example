package ru.virgil.spring.example.chat

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatMessageRepository : CrudRepository<ChatMessage, UUID>
