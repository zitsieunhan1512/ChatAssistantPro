package app.mbl.hcmute.chatApp.ui.features.chat.chatKit

import android.content.Context
import android.util.AttributeSet
import com.stfalcon.chatkit.messages.MessagesList

class MessagesView(context: Context?, attrs: AttributeSet?, defStyle: Int) : MessagesList(context, attrs, defStyle) {

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null, 0)

    override fun setAdapter(adapter: Adapter<*>?) { //Don't use this function.
        //override this to avoid can not render layout issue.
    }
}