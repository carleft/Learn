package com.tb.learn.fileexplorer.adapter

class SinglePresenterSelector(private val mPresenter: Presenter): PresenterSelector() {


    override fun getPresenter(item: Any?): Presenter {
        return mPresenter
    }

    override fun getPresenters(): ArrayList<Presenter> {
        return arrayListOf(mPresenter)
    }
}