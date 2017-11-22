package com.jeremy.customtextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * 自定义TextView
 */
class CustomTextView : View {

    private var mExampleString: String? = null
    private var mExampleColor = Color.RED
    private var mExampleDimension = 0f

    var exampleDrawable: Drawable? = null

    private var mTextPaint: TextPaint? = null
    private var mTextWidth: Float = 0.toFloat()
    private var mTextHeight: Float = 0.toFloat()
    private var mTextRect: Rect? = null

    var exampleString: String?
        get() = mExampleString
        set(exampleString) {
            mExampleString = exampleString
            invalidateTextPaintAndMeasurements()
        }

    var exampleColor: Int
        get() = mExampleColor
        set(exampleColor) {
            mExampleColor = exampleColor
            invalidateTextPaintAndMeasurements()
        }

    var exampleDimension: Float
        get() = mExampleDimension
        set(exampleDimension) {
            mExampleDimension = exampleDimension
            invalidateTextPaintAndMeasurements()
        }

    /**
     * 在代码new时候调用
     */
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    /**
     * 在布局layout中使用(调用)
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    /**
     * 在布局layout中使用,且使用style
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.CustomTextView, defStyle, 0)

        mExampleString = a.getString(R.styleable.CustomTextView_exampleString)
        mExampleColor = a.getColor(R.styleable.CustomTextView_exampleColor, mExampleColor)
        mExampleDimension = a.getDimension(R.styleable.CustomTextView_exampleDimension, mExampleDimension)

        if (a.hasValue(R.styleable.CustomTextView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(R.styleable.CustomTextView_exampleDrawable)
            exampleDrawable!!.callback = this
        }

        a.recycle()


        mTextPaint = TextPaint()
        mTextPaint!!.flags = Paint.ANTI_ALIAS_FLAG

        mTextRect = Rect()

        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        mTextPaint!!.textSize = mExampleDimension
        mTextPaint!!.color = mExampleColor
        mTextWidth = mTextPaint!!.measureText(mExampleString)

        val fontMetrics = mTextPaint!!.fontMetrics
        mTextHeight = fontMetrics.bottom - fontMetrics.top

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        if (exampleDrawable != null) {
            exampleDrawable!!.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight)
            exampleDrawable!!.draw(canvas)
        }

        mTextPaint!!.getTextBounds(mExampleString!!, 0, mExampleString!!.length, mTextRect)
        //获取x开始位置
        val x = paddingLeft
        val fontMetricsInt = mTextPaint!!.fontMetricsInt
        //控件的一半高度到baseline的距离
        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        //baseline
        val baselineY = height / 2 + dy

        canvas.drawText(mExampleString!!, x.toFloat(), baselineY.toFloat(), mTextPaint!!)
    }

    /**
     * 测量View的大小
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getMeasureWidth(widthMeasureSpec)
        val height = getMeasureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    /**
     * 获取测量宽度
     */
    private fun getMeasureWidth(widthMeasureSpec: Int): Int {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        return when (widthMode) {
            MeasureSpec.AT_MOST -> {
                paddingLeft + mTextWidth.toInt() + paddingRight
            }
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.UNSPECIFIED -> {
                widthSize
            }
            else -> {
                widthSize
            }
        }
    }

    /**
     * 获取测量高度
     */
    private fun getMeasureHeight(heightMeasureSpec: Int): Int {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        return when (heightMode) {
            MeasureSpec.AT_MOST -> {
                paddingTop + mTextHeight.toInt() + paddingBottom
            }
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.UNSPECIFIED -> {
                heightSize
            }
            else -> {
                heightSize
            }
        }
    }

}
