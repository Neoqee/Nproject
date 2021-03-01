package com.neoqee.viewmodule

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class TaijiView: View {

    private var useWidth = 0f
    private var mWidth = 0
    private var mHeight = 0
    private var objectAnimator: ObjectAnimator? = null

    constructor(context: Context):super(context){
    }

    constructor(context: Context,attr: AttributeSet) : super(context,attr) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.FILL
        }
        val center = (useWidth / 2).toFloat()
        val path = Path()
//        val rectF = RectF(center - 100f,center - 100f,center + 100f,center + 100f)
//        val rectFTop = RectF(center - 50f,center -50f - 50f,center + 50f,center)
//        val rectFBottom = RectF(center - 50f,center,center + 50f,center + 50f + 50f)
        val rectF = RectF(0f,0f,useWidth,useWidth)
        val rectFTop = RectF(useWidth / 4,0f,center + useWidth / 4,center)
        val rectFBottom = RectF(useWidth / 4,center,center + useWidth / 4,useWidth)
        path.addArc(rectF,-90f,-180f)
        path.arcTo(rectFBottom,90f,180f)
        path.arcTo(rectFTop,90f,-180f)
        canvas?.drawPath(path, paint)
        val path2 = Path()
        path2.arcTo(rectF,-90f,180f)
        path2.arcTo(rectFBottom,90f,180f)
        path2.arcTo(rectFTop,90f,-180f)
        paint.color = Color.WHITE
        canvas?.drawPath(path2,paint)
        canvas?.drawCircle(center,useWidth / 4,useWidth / 10,paint)
        paint.color = Color.BLACK
        canvas?.drawCircle(center,center + useWidth / 4,useWidth / 10,paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        useWidth = mWidth.toFloat()
        if (mWidth > mHeight){
            useWidth = mHeight.toFloat()
        }
    }

    fun createAnimation(){
        if (objectAnimator == null){
            objectAnimator = ObjectAnimator.ofFloat(this,"rotation",0f,360f)
                .apply {
                    duration = 1000L
                    interpolator = LinearInterpolator()
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                    start()
                }
        }else{
            objectAnimator!!.resume()
        }
    }

    fun stopAnimation(){
        objectAnimator?.pause()
    }

    fun cleanAnimation(){
        objectAnimator?.end()
    }

}