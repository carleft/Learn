package com.tb.learn.fileexplorer.page.fragment

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tb.learn.fileexplorer.R
import com.tb.learn.fileexplorer.adapter.Presenter

class ListPageFragment: BasePageFragment() {

    private lateinit var mRecyclerList: RecyclerView

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_page
    }

    override fun initView(root: View) {
        mRecyclerList = root.findViewById(R.id.fragment_list_page_recyclerview)

    }

    inner class ListPresenter: Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Any?) {
            TODO("Not yet implemented")
        }

    }
}