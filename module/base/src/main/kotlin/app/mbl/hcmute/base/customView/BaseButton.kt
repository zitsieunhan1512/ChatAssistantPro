package app.mbl.hcmute.base.customView

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.Button
import androidx.core.content.withStyledAttributes
import app.mbl.hcmute.base.R

@SuppressLint("AppCompatCustomView")
class BaseButton(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : Button(ContextThemeWrapper(context, R.style.WAC_BUTTON), attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var maxWidthPercent = 0.75f

    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.OneUiButton) {
                maxWidthPercent = getFloat(R.styleable.OneUiButton_maxWidthPercent, maxWidthPercent)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        if (maxWidthPercent < 1) {
            val widthSize = MeasureSpec.getSize(widthMeasureSpec)
            val buttonMaxWidth = (context.resources.displayMetrics.widthPixels * maxWidthPercent).toInt()
            if (widthSize > buttonMaxWidth) {
                val widthMode = MeasureSpec.getMode(widthMeasureSpec)
                widthSpec = MeasureSpec.makeMeasureSpec(buttonMaxWidth, widthMode)
            }
        }
        super.onMeasure(widthSpec, heightMeasureSpec)
    }

}