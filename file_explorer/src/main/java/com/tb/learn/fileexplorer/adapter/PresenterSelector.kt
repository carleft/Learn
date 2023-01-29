package com.tb.learn.fileexplorer.adapter
import androidx.recyclerview.widget.RecyclerView

abstract class PresenterSelector() {

    abstract fun getPresenter(item: Any?): Presenter

    abstract fun getPresenters(): ArrayList<Presenter>
}