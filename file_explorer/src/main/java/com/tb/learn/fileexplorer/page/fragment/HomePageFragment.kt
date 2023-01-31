package com.tb.learn.fileexplorer.page.fragment

import android.view.View
import android.widget.Button
import com.tb.learn.fileexplorer.R
import com.tb.learn.fileexplorer.page.PageHelper
import com.tb.tools.TBLog
import com.tb.tools.AsyncTest
import java.util.concurrent.Executors

class HomePageFragment: BasePageFragment() {

    companion object {
        private const val TAG = "HomePageFragment"
        private val executor = Executors.newFixedThreadPool(4)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_page
    }

    override fun initView(root: View) {

        root.findViewById<Button>(R.id.fragment_home_page_btn_file_list).setOnClickListener {
            PageHelper.addPage(PageHelper.Type.LIST)
        }

        root.findViewById<Button>(R.id.fragment_home_page_btn_executor_test).setOnClickListener {
            AsyncTest.getInstance().AsyncImpl4 {
                TBLog.e(TAG, "$it, currentThread = ${Thread.currentThread()}")
            }
            TBLog.e(TAG, "async job is running")
        }
    }
}