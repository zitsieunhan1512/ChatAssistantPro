package app.mbl.hcmute.chatApp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.BOOKMARK_TABLE
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.CONVERSATION_TABLE
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.MESSAGE_TABLE
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage
import androidx.room.Update as Update

@Dao
interface ChatDAO {
    //Conversation
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createConversation(conversation: Conversation)

    @Query("SELECT * FROM $CONVERSATION_TABLE ORDER BY lastUpdated DESC")
    fun getConversations(): LiveData<List<Conversation>>

    @Query("SELECT * FROM $CONVERSATION_TABLE WHERE id = :convId")
    fun getConversation(convId: Long): Conversation

    @Query("SELECT * FROM $CONVERSATION_TABLE WHERE title LIKE '%' || :searchKeyword || '%'")
    fun searchConversation(searchKeyword: String): LiveData<List<Conversation>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateConversation(conversation: Conversation)

    @Delete
    fun deleteConversation(conversation: Conversation)

    //Message
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createMessage(message: LocalChatMessage)

    @Query("SELECT * FROM $MESSAGE_TABLE WHERE conversationId = :conversationId")
    fun getMessages(conversationId: Long): List<LocalChatMessage>

    @Query("SELECT * FROM $MESSAGE_TABLE WHERE messageId = :messId")
    fun getMessageById(messId: String): LocalChatMessage

    //Bookmark
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createBookmark(bookmark: ChatBookmark)

    @Query("SELECT * FROM $BOOKMARK_TABLE WHERE content LIKE '%' || :searchKeyword || '%'")
    fun searchBookmark(searchKeyword: String): LiveData<List<ChatBookmark>>

    @Query("SELECT * FROM $BOOKMARK_TABLE ORDER BY createdTime DESC")
    fun getBookmarks(): LiveData<List<ChatBookmark>>

    @Delete
    fun deleteBookmark(bookmark: ChatBookmark)
}