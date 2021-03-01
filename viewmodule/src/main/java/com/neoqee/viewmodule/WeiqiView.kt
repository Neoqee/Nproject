package com.neoqee.viewmodule

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.view.View

class WeiqiView : View, ValueAnimator.AnimatorUpdateListener {

    private var mWidth = 0
    private var mHeight = 0
    private var useWidth = 0
    private var minWidth = 0f

    private var ball: ShapeHolder? = null
    private var ball1: ShapeHolder? = null
    private var ballHolder: QiziXYHolder? = null
    private var ballHolder1: QiziXYHolder? = null

    private var leftColor = Color.BLACK
    private var rightColor = Color.WHITE
    private var qipanColor = Color.BLACK

    private var bounceAnim: ValueAnimator? = null
    private var bounceAnim1: ValueAnimator? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {}

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint: Paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            strokeWidth = 4f
            isAntiAlias = true
        }
        canvas?.apply {
            minWidth = (useWidth / 10).toFloat()
            paint.color = qipanColor
            if (ball == null) {
                ball = createBall(minWidth * 3, minWidth * 3, leftColor)
                ballHolder = QiziXYHolder(ball!!)
            }
            if (ball1 == null) {
                ball1 = createBall(minWidth * 7, minWidth * 7, rightColor)
                ballHolder1 = QiziXYHolder(ball1!!)
            }
            // 画棋盘
            drawLine(minWidth, minWidth * 3, 9 * minWidth, minWidth * 3, paint)
            drawLine(minWidth, minWidth * 5, 9 * minWidth, minWidth * 5, paint)
            drawLine(minWidth, minWidth * 7, 9 * minWidth, minWidth * 7, paint)

            drawLine(minWidth * 3, minWidth, minWidth * 3, 9 * minWidth, paint)
            drawLine(minWidth * 5, minWidth, minWidth * 5, 9 * minWidth, paint)
            drawLine(minWidth * 7, minWidth, minWidth * 7, 9 * minWidth, paint)

            paint.strokeWidth = 8f

            drawLine(minWidth, minWidth, minWidth * 9, minWidth, paint)
            drawLine(minWidth, minWidth * 9, minWidth * 9, minWidth * 9, paint)

            drawLine(minWidth, minWidth, minWidth, minWidth * 9, paint)
            drawLine(minWidth * 9, minWidth, minWidth * 9, minWidth * 9, paint)

            drawPoint(minWidth, minWidth, paint)
            drawPoint(minWidth, minWidth * 9, paint)
            drawPoint(minWidth * 9, minWidth, paint)
            drawPoint(minWidth * 9, minWidth * 9, paint)

            save()
            canvas.translate(ball!!.x, ball!!.y)
            ball!!.shape?.draw(this)
            restore()

            save()
            canvas.translate(ball1!!.x, ball1!!.y)
            ball1!!.shape?.draw(this)
            restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        useWidth = if (mWidth <= mHeight) mWidth else mHeight
    }

    private fun createBall(x: Float, y: Float, color: Int): ShapeHolder {
        val circle = OvalShape()
        circle.resize(useWidth / 8f, useWidth / 8f)
        val drawable = ShapeDrawable(circle)
        val shapeHolder = ShapeHolder(drawable)
        shapeHolder.x = x - useWidth / 16f
        shapeHolder.y = y - useWidth / 16f
        val paint = drawable.paint
        paint.color = color
        val gradient = RadialGradient(
            useWidth / 16f, useWidth / 16f,
            useWidth / 8f, color, Color.GRAY, Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        shapeHolder.paint = paint
        return shapeHolder
    }

    private fun createAnimation() {
        if (bounceAnim == null) {
            val lstartXY = XYHolder(minWidth * 3 - minWidth / 16f, minWidth * 3 - minWidth / 16f)
            val processXY = XYHolder(minWidth * 7 - minWidth / 16f, minWidth * 3 - minWidth / 16f)
            val lendXY = XYHolder(minWidth * 7 - minWidth / 16f, minWidth * 7 - minWidth / 16f)
            bounceAnim = ObjectAnimator.ofObject(
                ballHolder, "xY", XYEvaluator(),
                lstartXY, processXY, lendXY, lstartXY
            )
            bounceAnim!!.apply {
                duration = 2000L
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                addUpdateListener(this@WeiqiView)
            }
        }
        if (bounceAnim1 == null) {
            val lstartXY = XYHolder(minWidth * 7 - minWidth / 16f, minWidth * 7 - minWidth / 16f)
            val processXY = XYHolder(minWidth * 3 - minWidth / 16f, minWidth * 7 - minWidth / 16f)
            val lendXY = XYHolder(minWidth * 3 - minWidth / 16f, minWidth * 3 - minWidth / 16f)
            bounceAnim1 = ObjectAnimator.ofObject(
                ballHolder1, "xY", XYEvaluator(),
                lstartXY, processXY, lendXY, lstartXY
            )
            bounceAnim1!!.apply {
                duration = 2000L
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                addUpdateListener(this@WeiqiView)
            }
        }
    }

    override fun onAnimationUpdate(p0: ValueAnimator?) {
        invalidate()
    }

    fun startAnimation() {
        Log.i("Neoqee", "开始动画")
        createAnimation()
        val animatorSet = AnimatorSet()
        animatorSet.play(bounceAnim).with(bounceAnim1)
        animatorSet.start()
    }

}

class ShapeHolder(var shape: ShapeDrawable) {
    var x = 0f
    var y = 0f

    //    var shape: ShapeDrawable? = null
    var color = 0
    var gradient: RadialGradient? = null
    var alpha = 1f
    var paint: Paint? = null
}

class QiziXYHolder(var mBall: ShapeHolder) {
    fun setXY(ball: XYHolder ) {
        mBall.x = ball.x
        mBall.y = ball.y
    }

    fun getXY(): XYHolder {
        return XYHolder(mBall.x, mBall.y)
    }
}

data class XYHolder(var x: Float, var y: Float) {

}

class XYEvaluator : TypeEvaluator<Any> {
    override fun evaluate(p0: Float, p1: Any?, p2: Any?): Any {
        val startXY = p1!! as XYHolder
        val endXY = p2!! as XYHolder
        return XYHolder(
            startXY.x + p0 * (endXY.x - startXY.x),
            startXY.y + p0 * (endXY.y - startXY.y)
        )
    }

}