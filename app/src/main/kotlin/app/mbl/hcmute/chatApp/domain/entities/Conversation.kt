package app.mbl.hcmute.chatApp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.mbl.hcmute.chatApp.data.local.room.DbConstants

@Entity(tableName = DbConstants.CONVERSATION_TABLE)
data class Conversation(
    @PrimaryKey
    val id: Long = -1,
    var title: String = "",
    val image: String = "",
    var userId: String = "", //not use now.
    var isPrivate: Boolean = false,
    var lastUpdated: Long,
    val createdTime: Long = System.currentTimeMillis(),
//    @Relation(parentColumn = "id", entityColumn = "conversationId")
//    var chatMessages: List<LocalChatMessage> = mutableListOf()
)
