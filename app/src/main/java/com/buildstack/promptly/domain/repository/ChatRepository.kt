package com.buildstack.promptly.domain.repository

import com.buildstack.promptly.domain.model.Chat
import com.buildstack.promptly.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllChats(): Flow<List<Chat>>
    suspend fun getChatById(chatId: Long): Chat?
    suspend fun createChat(title: String): Long
    suspend fun updateChatTitle(chatId: Long, newTitle: String)
    suspend fun deleteChat(chat: Chat)
    suspend fun clearAllChats()

    fun getMessagesForChat(chatId: Long): Flow<List<Message>>
    suspend fun addMessage(chatId: Long, role: String, content: String)
}
