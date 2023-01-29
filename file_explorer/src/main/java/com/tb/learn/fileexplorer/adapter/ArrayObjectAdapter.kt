package com.tb.learn.fileexplorer.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ArrayObjectAdapter private constructor(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mPresenterSelector: PresenterSelector? = null
    private val mDataList = ArrayList<Any?>()

    constructor(presenter: Presenter): this() {
        mPresenterSelector = SinglePresenterSelector(presenter)
    }

    constructor(presenterSelector: PresenterSelector): this() {
        mPresenterSelector = presenterSelector
    }

    //添加元素
    fun add(item: Any) {
        add(mDataList.size, item)
    }

    fun add(position: Int, item: Any) {
        mDataList.add(position, item)
        notifyItemRangeInserted(position, 1)
    }

    //移除元素
    fun remove(item: Any) {
        remove(mDataList.indexOf(item))
    }

    fun remove(position: Int) {
        if (position in 0 until mDataList.size) {
            mDataList.remove(position)
            notifyItemRangeRemoved(position, 1)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //根据PresenterSelector创建ViewHolder，否则返回空ViewHolder
        return (mPresenterSelector?.getPresenter(mDataList[viewType])?.onCreateViewHolder(parent)) ?:
                object: RecyclerView.ViewHolder(View(parent.context)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //根据PresenterSelector绑定
        val item = mDataList[position]
        mPresenterSelector?.getPresenter(item)?.onBindViewHolder(holder, item)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}