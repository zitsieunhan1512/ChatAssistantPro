package app.mbl.hcmute.chatApp.ui.features.chat.chatKit

import android.view.View
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.utils.DateFormatter

class MarkdownIncomingTextMessageViewHolder<T : IMessage>(view: View, payload: Any?) : MessageHolders.IncomingTextMessageViewHolder<T>(view, payload) {

    override fun onBind(message: T) {
        if (time != null) {
            time.text = DateFormatter.format(message.createdAt, DateFormatter.Template.TIME)
        }

        if (userAvatar != null) {
            val isAvatarExists = imageLoader != null && message.user.avatar != null && message.user.avatar.isNotEmpty()
            userAvatar.visibility = if (isAvatarExists) View.VISIBLE else View.GONE
            if (isAvatarExists) {
                imageLoader.loadImage(userAvatar, message.user.avatar, null)
            }
        }

        if (bubble != null) {
            bubble.isSelected = isSelected
        }

        text?.apply {
            val markdown = MarkDownProvider.instance
            if (markdown == null) MarkDownProvider.initMarkDown(itemView.context.applicationContext)
            MarkDownProvider.instance?.setMarkdown(this, message.text)
        }
    }
}