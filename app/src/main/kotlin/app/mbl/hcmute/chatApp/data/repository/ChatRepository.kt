package app.mbl.hcmute.chatApp.data.repository

import androidx.lifecycle.LiveData
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage

interface ChatRepository {
    //Conversation
    fun getConversations(): LiveData<List<Conversation>>
    fun getConversation(convId: Long): Conversation
    fun createConversation(conversation: Conversation)
    fun searchConversation(searchQuery: String): LiveData<List<Conversation>>
    fun updateConversation(conversation: Conversation)
    fun deleteConversation(conversation: Conversation)

    //Message
    suspend fun createMessage(message: LocalChatMessage)
    fun getMessages(conversationId: Long): List<LocalChatMessage>
    fun getMessageById(messId: String): LocalChatMessage

    //Bookmark
    fun createBookmark(bookmark: ChatBookmark)
    fun searchBookmark(searchKeyword: String): LiveData<List<ChatBookmark>>
    fun getBookmarks(): LiveData<List<ChatBookmark>>
    fun deleteBookmark(bookmark: ChatBookmark)
}