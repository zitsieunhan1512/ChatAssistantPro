package app.mbl.hcmute.base.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan


class CustomAlignmentSpan : ReplacementSpan {
    private var color = -1
    private var position: Int

    constructor(position: Int) {
        this.position = position
    }

    constructor(position: Int, color: Int) {
        this.position = position
        this.color = color
    }

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return 0
    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, defaultX: Float, top: Int, defaultY: Int, bottom: Int, paint: Paint) {
        var x = 0f
        var y = 0f
        when (position) {
            RIGHT_TOP -> {
                x = canvas.width - paint.measureText(text, start, end)
                y = paint.textSize
            }
            RIGHT_CENTER -> {
                x = canvas.width - paint.measureText(text, start, end)
                y = canvas.height.toFloat() / 2
            }
            RIGHT_BOTTOM -> {
                x = canvas.width - paint.measureText(text, start, end)
                y = canvas.height.toFloat() - paint.fontMetricsInt.bottom
            }
            RIGHT_DEFAULT -> {
                x = canvas.width - paint.measureText(text, start, end)
                y = defaultY.toFloat()
            }
        }
        if (color != -1) paint.color = color
        if (text != null) {
            canvas.drawText(text, start, end, x, y, paint)
        }
    }

    companion object {
        const val RIGHT_TOP = 1
        const val RIGHT_CENTER = 2
        const val RIGHT_BOTTOM = 3
        const val RIGHT_DEFAULT = 4
    }
}