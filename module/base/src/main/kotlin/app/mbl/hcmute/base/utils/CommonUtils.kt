package app.mbl.hcmute.base.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


fun Context.checkPermissions(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.buildUri(
    appName: String,
    displayName: String,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
): Uri =
    try {
        val ext = compressFormat.toFileExt()
        val resolver: ContentResolver = contentResolver

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, convertCompressFormatToMimeType(compressFormat))
        }

        if (Build.VERSION.SDK_INT >= 29) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/" + appName)
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/" + appName).toString()
            val image = File(imagesDir, "$displayName$ext")
            Uri.fromFile(image)
        }
    } catch (e: IOException) {
        throw RuntimeException("Failed to create temp file for output image", e)
    }


fun Context.buildInternalStorageCacheFilePath(
    displayName: String,
    folder: String,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
): String {
    val path = cacheDir.absolutePath + "/" + folder
    val direct = File(path)

    if (!direct.exists()) {
        direct.mkdir()
    }

    return path + "/" + displayName + compressFormat.toFileExt()
}

fun Bitmap.CompressFormat.toFileExt(): String {
    return when (this) {
        Bitmap.CompressFormat.JPEG -> ".jpg"
        Bitmap.CompressFormat.PNG -> ".png"
        else -> ".webp"
    }
}

fun Context.downloadImage(
    imageURL: String,
    customOutputUri: Uri,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    onSuccess: (Uri?) -> Unit,
    onFailed: () -> Unit,
) {
    Glide.with(this)
        .load(imageURL)
        .into(object : CustomTarget<Drawable?>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                val bitmap: Bitmap = (resource as BitmapDrawable).bitmap
                try {
                    val uri = writeBitmapToUri(bitmap, customOutputUri, compressFormat, 100)
                    if (uri != null) onSuccess(uri)
                    else onFailed()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Timber.tag("CommonUtils").e("${e.message}")
                    onFailed()
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                onFailed()
                kotlin.runCatching { contentResolver.delete(customOutputUri, null, null) }
            }
        })
}

fun Context.getRealPathFromURI(contentUri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor? = contentResolver.query(contentUri, proj, null, null, null)
    val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor?.moveToFirst()
    val path = columnIndex?.let { cursor.getString(it) }
    cursor?.close()
    return path
}


fun convertCompressFormatToMimeType(compressFormat: Bitmap.CompressFormat): String {
    return when (compressFormat) {
        Bitmap.CompressFormat.JPEG -> "image/jpeg"
        Bitmap.CompressFormat.PNG -> "image/png"
        else -> "image/webp"
    }
}

private const val WRITE_AND_TRUNCATE = "wt"

@Throws(FileNotFoundException::class)
fun Context.writeBitmapToUri(
    bitmap: Bitmap,
    customOutputUri: Uri,
    compressFormat: Bitmap.CompressFormat,
    compressQuality: Int = 100,
): Uri? {
    return contentResolver.openOutputStream(customOutputUri, WRITE_AND_TRUNCATE).use {
        val isSuccess = bitmap.compress(compressFormat, compressQuality, it)
        if (isSuccess) customOutputUri.also { Timber.tag("CommonUtils").d("save image success: $customOutputUri") }
        else null.also { Timber.tag("CommonUtils").d("save image failed") }
    }
}