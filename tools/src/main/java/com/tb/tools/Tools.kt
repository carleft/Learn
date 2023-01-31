package com.tb.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "Tools"

/**
 * 多变量判空函数
 */

fun <R, A> whenAllNotNull(param1: A?,
                          block: (param1: A) -> R): R? {
    return if (param1 != null)
        block(param1)
    else
        null
}

fun <R, A, B> whenAllNotNull(param1: A?, param2: B?,
                             block: (param1: A, param2: B) -> R): R? {
    return if (param1 != null && param2 != null)
        block(param1, param2)
    else
        null
}

fun <R, A, B, C> whenAllNotNull(param1: A?, param2: B?, param3: C?,
                             block: (param1: A, param2: B, param3: C) -> R): R? {
    return if (param1 != null && param2 != null && param3 != null)
        block(param1, param2, param3)
    else
        null
}

fun <R, A, B, C, D> whenAllNotNull(param1: A?, param2: B?, param3: C?, param4: D?,
                                block: (param1: A, param2: B, param3: C, param4: D) -> R): R? {
    return if (param1 != null && param2 != null && param3 != null && param4 != null)
        block(param1, param2, param3, param4)
    else
        null
}

fun <R, A, B, C, D, E> whenAllNotNull(param1: A?, param2: B?, param3: C?, param4: D?, param5: E?,
                                   block: (param1: A, param2: B, param3: C, param4: D, param5: E) -> R): R? {
    return if (param1 != null && param2 != null && param3 != null && param4 != null && param5 != null)
        block(param1, param2, param3, param4, param5)
    else
        null
}

/**
 *  通过BitmapFactory读取Bitmap
 *  @param path 文件路径
 *  @param targetWidth 目标宽度
 *  @param targetHeight 目标高度
 */
suspend fun getBitmapByFactory(path: String, targetWidth: Int, targetHeight: Int): Bitmap? {

    TBLog.d(TAG, "getBitmapByFactory, targetWidth = $targetWidth, targetHeight = $targetHeight")

    if (targetWidth <= 0 || targetHeight <= 0) return null

    //图片读取选项
    val option = BitmapFactory.Options()
    //缩小图片读取大小，避免Canvas: trying to draw too large bitmap

    //inPreferredConfig表示图片解码时使用的颜色模式，也就是图片中每个像素颜色的表示方式
    //默认为ARGB_8888
    option.inPreferredConfig = Bitmap.Config.ARGB_8888

    //inJustDecodeBounds为true时，不返回bitmap，但是可以通过option查询Bitmap的outWidth和outHeight
    option.inJustDecodeBounds = true

    withContext(Dispatchers.IO) {
        BitmapFactory.decodeFile(path, option)
    }

    if (option.outWidth <= 0 || option.outHeight <= 0) return null

    //目标缩放倍数
    var sampleSize = 1
    while (targetWidth * sampleSize < option.outWidth || targetHeight * sampleSize < option.outHeight) {
        sampleSize = sampleSize shl 1
    }

    TBLog.d(TAG, "getBitmapByFactory, outWidth = ${option.outWidth}, outHeight = ${option.outHeight}, sampleSize = $sampleSize")

    //inSampleSize表示缩放倍数，尽量为2的幂，如果不是也会被解码器调整为2的幂
    option.inSampleSize = sampleSize

    option.inJustDecodeBounds = false


    return withContext<Bitmap?>(Dispatchers.IO) {
        BitmapFactory.decodeFile(path, option)
    }
}

