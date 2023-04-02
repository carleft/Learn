package com.tb.customview

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatImageView
import com.tb.tools.MatrixAnimator
import com.tb.tools.TBLog

/**
 * 手指滑动缩放View
 */
class ScaleGestureImageView @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(mContext, attrs, defStyleAttr) {

    companion object {
        private const val TAG: String = "ScaleGestureImageView"
        //最大缩放倍数
        private const val MAX_SCALE: Float = 4.0f
    }

    //当前缩放矩阵
    private val mScaleMatrix: Matrix
    get() {
        return imageMatrix
    }
    //当前缩放矩阵值
    private val mScaleMatrixValue: FloatArray = FloatArray(9)
    //初始缩放倍数
    private var mInitScale: Float = 1f
    //是否为第一次收到onGlobalLayoutListener
    private var mOnce: Boolean = true

    /**
     * 可以通过ViewTreeObserver在布局完成可以获取真实尺寸的时候完成对图片的调整
     * 而OnGlobalLayoutListener是ViewTreeObserver的内部接口，当一个视图树的布局发生改变时，可以被ViewTreeObserver监听到
     */
    private val mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        if (drawable == null || !mOnce) {
            return@OnGlobalLayoutListener;
        }
        /**
         * drawable.intrinsicWidth 和 drawable.intrinsicHeight表示图片大小
         * width 和 height 表示
         */
        val scale = Math.min((width * 1.0f) / drawable.intrinsicWidth, (height * 1.0f) / drawable.intrinsicHeight)
        //移动到中心
        mScaleMatrix.postTranslate((width - drawable.intrinsicWidth) / 2f, (height - drawable.intrinsicHeight) / 2f)
        //缩放
        mScaleMatrix.postScale(scale, scale, width / 2f, height / 2f)
        imageMatrix = mScaleMatrix
        mInitScale = getCurrentScale()
        TBLog.d(TAG, "mInitScale = $mInitScale")
        mOnce = false
    }

    //手势检测监听
    private val mGestureDetector: GestureDetector = GestureDetector(context, object:
        GestureDetector.OnGestureListener {

        //用户按下屏幕就会触发
        override fun onDown(e: MotionEvent?): Boolean {
            TBLog.d(TAG, "onDown")
            return false
        }

        //如果是按下的时间超过瞬间，而且在按下的时候没有松开或者是拖动的，
        // 那么onShowPress就会执行
        override fun onShowPress(e: MotionEvent?) {
            TBLog.d(TAG, "onShowPress")
        }

        //用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            TBLog.d(TAG, "onSingleTapUp")
            return true
        }

        //用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            TBLog.d(TAG, "onScroll")
            return false
        }

        //用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
        override fun onLongPress(e: MotionEvent?) {
            TBLog.d(TAG, "onLongPress")
        }

        /**
         * 用户按下触摸屏、快速移动后松开
         * 由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
         * e1：第1个ACTION_DOWN MotionEvent
         * e2：最后一个ACTION_MOVE MotionEvent
         * velocityX：X轴上的移动速度，像素/秒
         * velocityY：Y轴上的移动速度，像素/秒
         */
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            TBLog.d(TAG, "onFling, velocityX = $velocityX, velocityY = $velocityY")
            return false
        }

    })

    //缩放手势检测
    private val mScaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, object:
        ScaleGestureDetector.OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (drawable == null) {
                return true
            }

            (detector?.scaleFactor)?.let {
                mScaleMatrix.postScale(it, it, width / 2f, height / 2f)
                imageMatrix = mScaleMatrix
            }
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            TBLog.d(TAG, "onScaleBegin")
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            TBLog.d(TAG, "onScaleEnd")
            val currentScale = getCurrentScale()
            //如果缩放倍速超过限制，恢复至最大限制
            if (currentScale > MAX_SCALE) {
                val scaleForFix = MAX_SCALE / currentScale
                val endMatrix = Matrix()
                endMatrix.set(mScaleMatrix)
                endMatrix.postScale(scaleForFix, scaleForFix, width / 2f, height / 2f)
                val matrixAnimator = MatrixAnimator(this@ScaleGestureImageView, mScaleMatrix, endMatrix)
                matrixAnimator.start()
            } else
            //缩放倍数小于初始值，恢复至初始值
            if (currentScale < mInitScale){
                val scaleForFix = MAX_SCALE / currentScale
                val endMatrix = Matrix()
                endMatrix.set(mScaleMatrix)
                endMatrix.postScale(scaleForFix, scaleForFix, width / 2f, height / 2f)
                val matrixAnimator = MatrixAnimator(this@ScaleGestureImageView, mScaleMatrix, endMatrix)
                matrixAnimator.start()
            }
        }
    })

    init {
        //设置矩阵缩放模式
        scaleType = ScaleType.MATRIX
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return mScaleGestureDetector.onTouchEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    /**
     * 获取当前缩放倍数
     */
    fun getCurrentScale(): Float {
        mScaleMatrix.getValues(mScaleMatrixValue)
        return mScaleMatrixValue[Matrix.MSCALE_X]
    }
}