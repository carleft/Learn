package com.tb.learn.fileexplorer.content

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.tb.learn.fileexplorer.MainApplication
import com.tb.tools.TBLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FileScanner {

    private val contentResolver: ContentResolver? = MainApplication.instance?.contentResolver
    private const val TAG: String = "FileScanner"

    private fun queryImg(): Cursor? {
        TBLog.e(TAG, "queryImg start")
        val timeStart = System.currentTimeMillis()

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

        return contentResolver?.query(uri, null, selection, null, sortOrder).also {
            TBLog.e(TAG, "queryImg end, elapsed time = ${System.currentTimeMillis() - timeStart}")
        }
    }

    /**
     * 扫描本机图片
     */
    suspend fun scanForImg(job: (FileScanResult) -> Unit) {
        //IO线程读取图片
        withContext(Dispatchers.IO) {
            TBLog.e(TAG, "scanForImg start")
            val timeStart = System.currentTimeMillis()
            queryImg()?.let {
                while (it.moveToNext()) {
                    //getColumnIndexOrThrow 如果获取不到对应的列则直接抛异常，因此最好只获取projection中的内容
                    val displayName = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                    val title: String = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
                    val mimeType: String = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
                    val path: String = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                    var modifyTimed: Long = it.getInt(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000L
                    val size: Int = it.getInt(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))

                    withContext(Dispatchers.Main) {
                        job(FileScanResult(title, path))
                    }

                }
                it.close()
            }
            TBLog.e(TAG, "scanForImg end, elapsed time = ${System.currentTimeMillis() - timeStart}")
        }
    }
}