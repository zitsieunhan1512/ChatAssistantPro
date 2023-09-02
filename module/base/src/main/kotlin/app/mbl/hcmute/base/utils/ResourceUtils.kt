package app.mbl.hcmute.base.utils

import androidx.fragment.app.Fragment

fun Fragment.getInteger(resId: Int): Int? = context?.resources?.getInteger(resId)

fun Fragment.getDimension(resId: Int): Int? = context?.resources?.getDimensionPixelSize(resId)