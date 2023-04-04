package com.tb.calculator

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tb.calculator.keyboard.CalculateUtil
import com.tb.calculator.keyboard.CalculatorButtonType
import com.tb.calculator.keyboard.CalculatorKeyBoardAdapter

class MainActivity: FragmentActivity() {

    private var currentText: StringBuffer = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calculator)
        initViews()
    }

    private fun initViews() {
        //初始化数字显示器
        val display: TextView = findViewById(R.id.calculator_display_tv)
        display.text = currentText

        //初始化键盘
        val keyboardRecyclerView: RecyclerView = findViewById(R.id.calculator_keyboard_recycler_view)
        //初始化适配器
        val adapter = CalculatorKeyBoardAdapter(this)
        //设置点击事件
        adapter.onItemClickListener = object: CalculatorKeyBoardAdapter.OnItemClickListener {
            override fun onClick(type: String) {
                when (type) {
                    CalculatorButtonType.ZERO -> currentText.append(0)
                    CalculatorButtonType.ONE -> currentText.append(1)
                    CalculatorButtonType.TWO -> currentText.append(2)
                    CalculatorButtonType.THREE -> currentText.append(3)
                    CalculatorButtonType.FOUR -> currentText.append(4)
                    CalculatorButtonType.FIVE -> currentText.append(5)
                    CalculatorButtonType.SIX -> currentText.append(6)
                    CalculatorButtonType.SEVEN -> currentText.append(7)
                    CalculatorButtonType.EIGHT -> currentText.append(8)
                    CalculatorButtonType.NINE -> currentText.append(9)
                    CalculatorButtonType.PLUS -> currentText.append("+")
                    CalculatorButtonType.SUBTRACT -> currentText.append("-")
                    CalculatorButtonType.MULTIPLY -> currentText.append("*")
                    CalculatorButtonType.DIVIDE -> currentText.append("÷")
                    CalculatorButtonType.POINT -> currentText.append(".")
                    CalculatorButtonType.CLEAR -> currentText.setLength(0)
                    CalculatorButtonType.DELETE -> currentText.deleteCharAt(currentText.length - 1)
                    CalculatorButtonType.EQUAL -> currentText = StringBuffer(CalculateUtil.calculateExpression(currentText.toString()).toString())
                }
                //更新显示
                display.text = currentText
            }

        }
        //设置适配器
        keyboardRecyclerView.adapter = adapter
        //设置布局管理器
        keyboardRecyclerView.layoutManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)


    }
}