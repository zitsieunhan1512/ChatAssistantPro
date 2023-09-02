package app.mbl.hcmute.chatApp.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.MESSAGE_TABLE
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

@Entity(
    tableName = MESSAGE_TABLE,
    foreignKeys = [ForeignKey(
        entity = Conversation::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("conversationId"),
        onDelete = ForeignKey.CASCADE //auto delete all chat message after delete conversation.
    )]
)
data class LocalChatMessage(
    @PrimaryKey()
    val messageId: String,
    var messageContent: String?,
    val messageAuthor: Author,
    val createdTime: Date = Date(),
    //foreign key on Conversation class
    var conversationId: Long = -1,
    val type: String? = null,
) : IMessage {
    override fun getId() = messageId
    override fun getText() = messageContent
    override fun getUser() = messageAuthor
    override fun getCreatedAt() = createdTime
}

fun createLocalMessage(message: String, author: Author, date: Date = Date()): LocalChatMessage {
    return LocalChatMessage(System.currentTimeMillis().toString(), message, author, date)
}

@OptIn(BetaOpenAI::class)
fun LocalChatMessage.toChatMessage(role: ChatRole = ChatRole.User): ChatMessage {
    return ChatMessage(role, messageContent ?: "", messageAuthor.authorId)
}

data class Author(val authorId: String, val authorName: String, val authorAvt: String) : IUser {
    override fun getId() = authorId
    override fun getName() = authorName
    override fun getAvatar() = authorAvt
}