package com.tb.learn.fileexplorer.page.fragment

import android.view.View
import android.widget.Button
import com.tb.learn.fileexplorer.R
import com.tb.learn.fileexplorer.page.PageHelper

class HomePageFragment: BasePageFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_page
    }

    override fun initView(root: View) {
        root.findViewById<Button>(R.id.fragment_home_page_btn).setOnClickListener {
            PageHelper.addPage(PageHelper.Type.LIST)
        }
    }
}