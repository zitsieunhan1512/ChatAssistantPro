package app.mbl.hcmute.chatApp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage

@Database(entities = [Conversation::class, LocalChatMessage::class, ChatBookmark::class], version = 1, exportSchema = true)
@TypeConverters(RoomConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDAO
}

object DbConstants {
    const val DATABASE_NAME = "chat_db"
    const val CONVERSATION_TABLE = "conversation_tb"
    const val MESSAGE_TABLE = "message_tb"
    const val BOOKMARK_TABLE = "bookmark_tb"
}