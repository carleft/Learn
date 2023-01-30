package com.tb.learn.fileexplorer.content

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import com.tb.learn.fileexplorer.MainApplication
import com.tb.tools.TBLog

object FileScanner {

    private val contentResolver: ContentResolver? = MainApplication.instance?.contentResolver
    private const val TAG: String = "FileScanner"

    fun queryOther(): ArrayList<FileScanResult> {

        //区分Provider的URI
        val uri = MediaStore.Files.getContentUri("external")

        //projection表示要返回的列内容，不填则返回所有列
        val projection = arrayOf(
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.MIME_TYPE,
        )

        //selection表示筛选条件，相当于WHERE，不填表示不筛选
        val selection: String = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.jpg'" +
        " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.png'" +
        " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.webp'" + ")";

        val selectionArgs = null

        //sortOrder表示排序方式，相当于Order By，升序（默认）在后面跟" ASC"，降序在后面跟" DESC"
        val sortOrder: String = MediaStore.Files.FileColumns.SIZE + " DESC"

        val cursor: Cursor? = contentResolver?.query(uri, null, selection, null, sortOrder)
        return parseCursor(cursor)
    }

    private fun parseCursor(cursor: Cursor?): ArrayList<FileScanResult> {
        val result: ArrayList<FileScanResult> = ArrayList()
        cursor?.let {
            while (it.moveToNext()) {
                //getColumnIndexOrThrow 如果获取不到对应的列则直接抛异常，因此最好只获取projection中的内容
                val displayName = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val title: String = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
                val mimeType: String = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                val path: String = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                var modifyTimed: Long = it.getInt(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000L
                val size: Int = it.getInt(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))

                //读取图片
                val option = BitmapFactory.Options()
                //缩小图片读取大小，避免Canvas: trying to draw too large bitmap
                option.inPreferredConfig = Bitmap.Config.ALPHA_8
                option.inJustDecodeBounds = false
                option.inSampleSize = 100

                BitmapFactory.decodeFile(path, option)?.let { bitmap ->
                    result.add(FileScanResult(title, path, bitmap))
                }
                TBLog.e(TAG, "displayName = $displayName, title = $title, mimeType = $mimeType, path = $path, modifyTimed = $modifyTimed, size = $size");
            }
            it.close()
        }
        return result
    }

    data class FileScanResult(val title: String, val path: String, val bitmap: Bitmap?)
}