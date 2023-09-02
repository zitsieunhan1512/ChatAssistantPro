package app.mbl.hcmute.chatApp.data.repository

import androidx.lifecycle.LiveData
import app.mbl.hcmute.chatApp.data.local.room.ChatDAO
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatDao: ChatDAO) : ChatRepository {
    override fun getConversations(): LiveData<List<Conversation>> {
        return chatDao.getConversations()
    }

    override fun getConversation(convId: Long): Conversation {
        return chatDao.getConversation(convId)
    }

    override fun createConversation(conversation: Conversation) {
        chatDao.createConversation(conversation)
    }

    override fun searchConversation(searchQuery: String): LiveData<List<Conversation>> {
        return chatDao.searchConversation(searchQuery)
    }

    override fun updateConversation(conversation: Conversation) {
        chatDao.updateConversation(conversation)
    }

    override fun deleteConversation(conversation: Conversation) {
        chatDao.deleteConversation(conversation)
    }

    override suspend fun createMessage(message: LocalChatMessage) {
        chatDao.createMessage(message)
    }

    override fun getMessages(conversationId: Long): List<LocalChatMessage> {
        return chatDao.getMessages(conversationId)
    }

    override fun getMessageById(messId: String): LocalChatMessage {
        return chatDao.getMessageById(messId)
    }

    override fun createBookmark(bookmark: ChatBookmark) {
        chatDao.createBookmark(bookmark)
    }

    override fun searchBookmark(searchKeyword: String): LiveData<List<ChatBookmark>> {
        TODO("Not yet implemented")
    }

    override fun getBookmarks(): LiveData<List<ChatBookmark>> {
        return chatDao.getBookmarks()
    }

    override fun deleteBookmark(bookmark: ChatBookmark) {
        chatDao.deleteBookmark(bookmark)
    }

}