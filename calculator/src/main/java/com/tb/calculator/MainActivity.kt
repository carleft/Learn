package com.tb.calculator

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tb.calculator.keyboard.CalculatorKeyBoardAdapter

class MainActivity: FragmentActivity() {

    private lateinit var mBtnAreaRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calculator)
        initViews()
    }

    private fun initViews() {
        mBtnAreaRV = findViewById(R.id.calculator_keyboard_recycler_view)
        //设置适配器
        mBtnAreaRV.adapter = CalculatorKeyBoardAdapter(this)
        //设置布局管理器
        mBtnAreaRV.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
    }
}