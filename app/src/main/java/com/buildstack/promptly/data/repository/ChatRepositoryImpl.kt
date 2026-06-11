package com.buildstack.promptly.data.repository

import com.buildstack.promptly.data.local.ChatDao
import com.buildstack.promptly.data.local.ChatEntity
import com.buildstack.promptly.data.local.MessageEntity
import com.buildstack.promptly.domain.model.Chat
import com.buildstack.promptly.domain.model.Message
import com.buildstack.promptly.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val dao: ChatDao
) : ChatRepository {

    override fun getAllChats(): Flow<List<Chat>> {
        return dao.getAllChats().map { entities ->
            entities.map { it.toChat() }
        }
    }

    override suspend fun getChatById(chatId: Long): Chat? {
        return dao.getChatById(chatId)?.toChat()
    }

    override suspend fun createChat(title: String): Long {
        val currentTime = System.currentTimeMillis()
        val chatEntity = ChatEntity(
            title = title,
            createdAt = currentTime,
            updatedAt = currentTime
        )
        return dao.insertChat(chatEntity)
    }

    override suspend fun updateChatTitle(chatId: Long, newTitle: String) {
        dao.getChatById(chatId)?.let {
            dao.updateChat(it.copy(title = newTitle, updatedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun deleteChat(chat: Chat) {
        dao.getChatById(chat.chatId)?.let {
            dao.deleteChat(it)
        }
    }

    override suspend fun clearAllChats() {
        dao.clearAllChats()
    }

    override fun getMessagesForChat(chatId: Long): Flow<List<Message>> {
        return dao.getMessagesForChat(chatId).map { entities ->
            entities.map { it.toMessage() }
        }
    }

    override suspend fun addMessage(chatId: Long, role: String, content: String) {
        val currentTime = System.currentTimeMillis()
        val messageEntity = MessageEntity(
            chatId = chatId,
            role = role,
            content = content,
            timestamp = currentTime
        )
        dao.insertMessage(messageEntity)
        
        // Update chat updatedAt time
        dao.getChatById(chatId)?.let {
            dao.updateChat(it.copy(updatedAt = currentTime))
        }
    }
}

// Extension functions for mapping
fun ChatEntity.toChat(): Chat {
    return Chat(
        chatId = chatId,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        messageId = messageId,
        chatId = chatId,
        role = role,
        content = content,
        timestamp = timestamp
    )
}
