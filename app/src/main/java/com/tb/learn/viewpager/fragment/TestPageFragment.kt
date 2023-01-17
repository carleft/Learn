package com.tb.learn.viewpager.fragment

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.tb.learn.R

class TestPageFragment(): BasePageFragment() {
    private var title: String = ""

    constructor(title: String): this() {
        this.title = title
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_test_page
    }

    override fun initView(root: View) {
        root.findViewById<TextView>(R.id.fragment_test_page_title).text = title
        root.findViewById<Button>(R.id.fragment_test_page_btn).setOnClickListener {
            Toast.makeText(this.context, toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun toString(): String {
        return "@${this.hashCode()}, title = $title"
    }
}