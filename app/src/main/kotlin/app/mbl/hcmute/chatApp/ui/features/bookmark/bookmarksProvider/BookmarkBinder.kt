package app.mbl.hcmute.chatApp.ui.features.bookmark.bookmarksProvider

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import mva3.adapter.ItemBinder
import mva3.adapter.ItemViewHolder
import java.text.SimpleDateFormat
import java.util.*


class BookmarkBinder(private val onItemClick: (convId: Long, messId: String) -> Unit) : ItemBinder<ChatBookmark, BookmarkBinder.ViewHolder>() {
    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(inflate(parent, R.layout.bookmark_item))
    }

    override fun bindViewHolder(holder: ViewHolder, item: ChatBookmark) {
        holder.bindItemData(item)
    }

    override fun canBindData(item: Any): Boolean {
        return item is ChatBookmark
    }

    override fun getSpanSize(maxSpanCount: Int): Int {
        return maxSpanCount
    }

    inner class ViewHolder(itemView: View) : ItemViewHolder<ChatBookmark>(itemView) {
        val tvTitle: TextView
        val tvTime: TextView
        val ivImage: ImageView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvTime = itemView.findViewById(R.id.tvTime)
            ivImage = itemView.findViewById(R.id.ivImage)
            itemView.setOnClickListener {
                onItemClick.invoke(item.conversationId, item.messageId)
            }
        }

        override fun getSwipeDirections(): Int {
            return ItemTouchHelper.START or ItemTouchHelper.END
        }

        fun bindItemData(item: ChatBookmark) {
            tvTitle.text = item.content.ifEmpty { "New Bookmark" }
            tvTime.text = item.createdTime.mapToDateTime()
            ivImage.setImageBitmap(tvTitle.text.toString().getFirstLetterBitmap())
        }
    }
}

//Return dateTimeFormat with format HH:mm:ss dd/MM/yyyy
private fun Number.mapToDateTime(): String {
    val date = Date(this.toLong())
    val format = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

fun String.getFirstLetterBitmap(): Bitmap {
    val letter = this.first().toString().toUpperCase(Locale.getDefault())

    val width = 240 // Độ rộng của ảnh
    val height = 240 // Chiều cao của ảnh
    val radius = 120f // Bán kính của ảnh

    val paint = Paint().apply {
        color = getRandomColor() // Lấy màu ngẫu nhiên
        isAntiAlias = true
        style = Paint.Style.FILL
        textSize = 120f
        textAlign = Paint.Align.CENTER
    }

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawCircle(radius, radius, radius, paint)
    paint.color = Color.WHITE
    canvas.drawText(letter, radius, radius + paint.textSize / 3, paint)

    return bitmap
}

private fun getRandomColor(): Int {
    val random = Random()
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}
