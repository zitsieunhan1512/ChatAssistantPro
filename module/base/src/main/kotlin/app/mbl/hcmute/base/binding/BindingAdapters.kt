package app.mbl.hcmute.base.binding

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import app.mbl.hcmute.base.utils.ViewUtils.Companion.getDynamicDialogWidth
import app.mbl.hcmute.base.utils.ViewUtils.Companion.getDynamicViewPadding
import app.mbl.hcmute.base.R
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import timber.log.Timber

class BindingAdapters {}

@BindingAdapter("showView")
fun showView(view: View, visible: Boolean? = null) {
    view.visibility = if (visible != null && visible) View.VISIBLE else View.GONE
}

@BindingAdapter("drawable")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("selected")
fun setSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("dynamicDialogWidth")
fun dynamicDialogWidth(view: View, isDynamic: Boolean) {
    Timber.d("dynamicDialogWidth", "Init dynamic binding: $isDynamic")
    if (isDynamic) {
        val layoutParams = view.layoutParams
        layoutParams.width = getDynamicDialogWidth()
        Timber.d("dynamicDialogWidth", "new width: ${layoutParams.width}")
        view.layoutParams = layoutParams
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@BindingAdapter("dynamicViewWidth")
fun View.dynamicViewWidth(isDynamic: Boolean) {
    Timber.d("dynamicViewWidth", "Init dynamic binding: $isDynamic")
    if (!(this.context as Activity).isInMultiWindowMode) {
        if (isDynamic) {
            val padding = getDynamicViewPadding()
            Timber.d("dynamicViewWidth", "padding: $padding")
            this.setPadding(padding, 0, padding, 0)
        }
    } else this.context.resources.getDimension(R.dimen.dimen_14dp_w).toInt().let { this.setPadding(it, 0, it, 0) }
}

@BindingAdapter("srcResId")
fun setImageWithResId(imageView: ImageView, resId: Int) {
    Glide.with(imageView.context).load(resId).centerCrop()
        .priority(Priority.IMMEDIATE)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(imageView)
}