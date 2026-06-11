package com.buildstack.promptly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val chatId: Long = 0,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long
)
