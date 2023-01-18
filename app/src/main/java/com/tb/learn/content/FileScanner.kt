package com.tb.learn.content

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.tb.learn.MainApplication

object FileScanner {

    private val contentResolver: ContentResolver? = MainApplication.instance?.contentResolver

    fun queryOther() {
        val selection: String = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.xls'" +
        " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.docx'" +
                " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.apk'" +
                " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.xlsx'" +
                " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.zip'" +
//                " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.%'" +
                " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.rar'" + ")";
        val cursor: Cursor? = contentResolver?.query(MediaStore.Files.getContentUri("external"), null, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
        parseCursor(cursor);
    }

    private fun parseCursor(cursor: Cursor?) {
        if (cursor == null) {
            return;
        }
        while (cursor.moveToNext()) {
            //String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE));
            val path: String = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
            var modifyTimed: Long = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000L
            val displayName: String = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
            val size: Int = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))

            Log.e("TB0232" ,"size " + size + "dispaly " + displayName + "path " + path);

        }
        cursor.close();
    }

}