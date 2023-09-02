package app.mbl.hcmute.chatApp.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.BOOKMARK_TABLE

@Entity(
    tableName = BOOKMARK_TABLE,
    foreignKeys = [ForeignKey(
        entity = LocalChatMessage::class,
        parentColumns = arrayOf("messageId"),
        childColumns = arrayOf("messageId"),
        onDelete = ForeignKey.CASCADE //auto delete all bookmark after delete chat message.
    )]
)
data class ChatBookmark(
    @PrimaryKey
    val id: String,
    val messageId: String,
    val conversationId: Long,
    val content: String,
    val createdTime: Long,
)