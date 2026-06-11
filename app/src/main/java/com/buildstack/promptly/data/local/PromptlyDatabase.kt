package com.buildstack.promptly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChatEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class PromptlyDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
