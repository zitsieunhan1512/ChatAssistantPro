package app.mbl.hcmute.base.ext

fun Any.getTagName(): Lazy<String> = lazy { this::class.java.simpleName }
