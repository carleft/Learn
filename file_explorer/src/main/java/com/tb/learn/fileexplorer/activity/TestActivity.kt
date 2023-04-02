package com.tb.learn.fileexplorer.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tb.learn.fileexplorer.R
import com.tb.customview.ScaleGestureImageView
import com.tb.tools.TBLog
import com.tb.tools.getBitmapByFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TestActivity: FragmentActivity() {


    companion object {
        const val TAG = "TestActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val mImgViewer = findViewById<ScaleGestureImageView>(R.id.activity_test_img)
        val path = intent.getStringExtra("path")
        path?.let {
            mImgViewer.post {
                val width = mImgViewer.width
                val height = mImgViewer.height
                TBLog.d(TAG, "width = $width, height = $height")

                MainScope().launch(Dispatchers.Main) {
                    getBitmapByFactory(it, width, height)?.let {
                        mImgViewer.setImageBitmap(it)
                        mImgViewer.visibility = View.VISIBLE
                    }
                }
            }
        }

    }
}