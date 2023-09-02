package app.mbl.hcmute.chatApp.ui.features.chat.chatKit

import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.messages.MessagesListAdapter.Wrapper

class ChatAdapter(senderId: String, messageHolders: MessageHolders? = MessageHolders(), imageLoader: ImageLoader? = null) :
    MessagesListAdapter<LocalChatMessage>(senderId, messageHolders, imageLoader) {

    fun lastNumberItems(takeItemCount: Int): MutableList<Wrapper<*>> {
        return items.reversed().takeLast(takeItemCount).toMutableList()
//            .removeFirstChatIfNullOrNotChat()
    }

    fun getItems(): MutableList<Wrapper<*>> {
        return items
    }
}

private fun <E : Any> MutableList<E>.removeFirstChatIfNullOrNotChat(): MutableList<E> {
    kotlin.runCatching {
        if ((this[0] as Wrapper<LocalChatMessage>).item.messageContent.isNullOrEmpty()) this.removeAt(0)
    }.getOrElse { this.removeAt(0) }
    return this
}