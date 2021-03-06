package com.linku.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.linku.domain.entity.Conversation
import com.linku.domain.entity.Message
import com.linku.domain.entity.User
import com.linku.domain.room.dao.ConversationDao
import com.linku.domain.room.dao.MessageDao
import com.linku.domain.room.dao.UserDao

@Database(
    entities = [User::class, Message::class, Conversation::class],
    version = 1,
    exportSchema = false
)
abstract class LinkUDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
}