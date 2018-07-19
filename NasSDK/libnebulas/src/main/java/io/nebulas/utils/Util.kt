package io.nebulas.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import java.io.ByteArrayOutputStream

/**
 * 星云工具类
 * Created by donald99 on 18/5/23.
 */

object Util {

    @JvmStatic
    fun getRandomCode(length: Int): String {
        var random = ""

        val abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

        for (i in 0 until length) {
            var v = Math.random() * abc.length
            v = Math.floor(v)
            random += abc[v.toInt()]
        }

        return random
    }

    fun getAppIconBytes(iconDrawable: Drawable): ByteArray {
        val bm = getAppIcon(iconDrawable) ?: return ByteArray(0)
        val bytes = bitmap2Bytes(bm)
        bm.recycle()
        return bytes
    }

    fun getAppIcon(iconDrawable: Drawable): Bitmap? {
        if (Build.VERSION.SDK_INT >= 26) {
            return getAppIconV26(iconDrawable)
        }
        if (iconDrawable is BitmapDrawable) {
            return iconDrawable.bitmap
        }
        return null
    }

    fun bitmap2Bytes(bitmap: Bitmap):ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @SuppressLint("NewApi")
    private fun getAppIconV26(iconDrawable: Drawable): Bitmap? {
        if (iconDrawable is BitmapDrawable) {
            return iconDrawable.bitmap
        } else if (iconDrawable is AdaptiveIconDrawable) {
            val drawableArray = arrayOf(iconDrawable.background, iconDrawable.foreground)
            val layerDrawable = LayerDrawable(drawableArray)
            val w = layerDrawable.intrinsicWidth
            val h = layerDrawable.intrinsicHeight
            val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)
            layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
            layerDrawable.draw(canvas)
            return bm
        }
        return null
    }
}
