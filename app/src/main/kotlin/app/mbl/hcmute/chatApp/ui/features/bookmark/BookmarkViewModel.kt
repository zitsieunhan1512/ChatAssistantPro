package app.mbl.hcmute.chatApp.ui.features.bookmark

import android.view.View
import androidx.lifecycle.viewModelScope
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.data.repository.ChatRepository
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val chatRepository: ChatRepository) : BaseViewModel() {

    val bookmarks = chatRepository.getBookmarks()

    fun createBookmark(bookmark: ChatBookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.createBookmark(bookmark)
        }
    }

    fun deleteBookmark(bookmark: ChatBookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.deleteBookmark(bookmark)
        }
    }
}