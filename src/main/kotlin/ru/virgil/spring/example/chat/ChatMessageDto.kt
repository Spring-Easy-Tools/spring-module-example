package ru.virgil.spring.example.chat

data class ChatMessageDto(
    val text: String? = null,
    val author: String? = null,
)
