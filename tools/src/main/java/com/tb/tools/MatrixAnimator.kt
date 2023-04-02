package com.tb.tools

import android.animation.ValueAnimator
import android.graphics.Matrix
import android.view.View
import android.widget.ImageView

/**
 * 矩阵变换动画
 */
class MatrixAnimator(
    view: ImageView,
    startMatrix: Matrix,
    endMatrix: Matrix,
    duration: Long = 300): ValueAnimator() {

    companion object {
        const val TAG = "MatrixAnimator"
    }

    //起始矩阵值
    var mStartValue: FloatArray = FloatArray(9)
    //中间过程矩阵值
    var mMiddleValue: FloatArray = FloatArray(9)
    //结束矩阵值
    var mEndValue: FloatArray = FloatArray(9)

    init {
        setFloatValues(0f, 1f)
        startMatrix.getValues(mStartValue)
        endMatrix.getValues(mEndValue)
        this.duration = duration
        addUpdateListener {
            val floatValue = it.animatedValue as Float
            TBLog.d(TAG, "updating, value = $floatValue")
            for (i in mMiddleValue.indices) {
                mMiddleValue[i] = mStartValue[i] + (mEndValue[i] - mStartValue[i]) * floatValue
            }
            val matrix = view.imageMatrix
            matrix.setValues(mMiddleValue)
            view.imageMatrix = matrix
            view.invalidate()
        }
    }

}