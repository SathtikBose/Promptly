package com.buildstack.promptly.domain.model

data class Message(
    val messageId: Long,
    val chatId: Long,
    val role: String,
    val content: String,
    val timestamp: Long
)
