package com.buildstack.promptly.domain.model

data class Chat(
    val chatId: Long,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
)
