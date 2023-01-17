package com.tb.learn.viewpager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tb.learn.R

abstract class BasePageFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(getLayoutId(), container, false)
        initView(view)
        return view
    }

    /**
     * 获取Fragment布局ID
     */
    open fun getLayoutId(): Int {
        return 0
    }

    /**
     * 初始化布局
     */
    open fun initView(root: View) {

    }

}