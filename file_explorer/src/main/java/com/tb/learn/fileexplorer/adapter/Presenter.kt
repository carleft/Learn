package com.tb.learn.fileexplorer.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class Presenter {
    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Any?)
}