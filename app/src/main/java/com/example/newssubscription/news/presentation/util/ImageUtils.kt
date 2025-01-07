package com.example.newssubscription.news.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.ByteArrayOutputStream


fun Uri.toBytArray(context: Context) =
    context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }

fun Bitmap.toBytArray():ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
