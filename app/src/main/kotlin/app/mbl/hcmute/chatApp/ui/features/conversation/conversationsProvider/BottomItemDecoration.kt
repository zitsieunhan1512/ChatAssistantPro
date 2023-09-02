package app.mbl.hcmute.chatApp.ui.features.conversation.conversationsProvider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == state.itemCount - 1) // Check if it's the last item
            outRect.bottom = 420 // Space(in pixels) to give after the last item for that floating button
    }
}