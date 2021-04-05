package com.neoqee.viewmodule

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CustomLayout : ViewGroup {

    constructor(context: Context): super(context)

    constructor(context: Context,attributeSet: AttributeSet): super(context,attributeSet)

    val header = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageResource(R.drawable.img_head)
        layoutParams = LayoutParams(MATCH_PARENT, 280.dp)
        addView(this)
    }

    val fab = FloatingActionButton(context).apply {
        setImageResource(R.drawable.img_votal)
        layoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            it.marginEnd = 12.dp
        }
        addView(this)
    }

    val avatar = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageResource(R.drawable.img_votal)
        layoutParams = MarginLayoutParams(48.dp, 48.dp).also {
            it.marginStart = 16.dp
            it.topMargin = 24.dp
        }
        addView(this)
    }

    val itemTitle = AppCompatTextView(context).apply {
        text = "Text Title"
        layoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            it.marginStart = 16.dp
            it.marginEnd = 16.dp
        }
        addView(this)
    }

    val itemMessage = AppCompatTextView(context).apply {
        text = "This is a long message. It contains multiple lines of text. There" +
                " will be an icon on the right side of it, so the text should not be laid" +
                " out in the area of the icon."
        layoutParams = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
            it.marginStart = 16.dp
            it.marginEnd = 16.dp
        }
        addView(this)
    }

    val reply = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        setImageResource(R.drawable.img_votal)
        layoutParams = MarginLayoutParams(24.dp, 24.dp).also {
            it.setMargins(24.dp,24.dp,24.dp,24.dp)
        }
        addView(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        header.autoMeasure()
        fab.autoMeasure()
        avatar.autoMeasure()
        reply.autoMeasure()
        val itemTextWidth = (measuredWidth
                - avatar.measuredWidthWithMargins
                - reply.measuredHeightWithMargins
                - itemTitle.marginLeft
                - itemTitle.marginRight)
        itemTitle.measure(
            itemTextWidth.toExactlyMeasureSpec(),
            itemTitle.defaultHeightMeasureSpec(this)
        )
        itemMessage.measure(
            itemTextWidth.toExactlyMeasureSpec(),
            itemMessage.defaultHeightMeasureSpec(this)
        )

        val max = (avatar.marginTop + itemTitle.measuredHeightWithMargins
                + itemMessage.measuredHeightWithMargins)
            .coerceAtLeast(avatar.measuredHeightWithMargins)
        val wrapContentHeight = header.measuredHeightWithMargins + max
        setMeasuredDimension(measuredWidth,wrapContentHeight)

    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        header.layout(x = 0,y = 0)
        fab.let { it.layout(x = it.marginRight, y= header.bottom - (it.measuredHeight / 2), fromRight = true) }
        avatar.let { it.layout(x = it.marginLeft, y = header.bottom + it.marginTop) }
        itemTitle.let { it.layout(x = avatar.right + it.marginLeft, y = avatar.top + it.marginTop) }
        itemMessage.let { it.layout(x = avatar.right + it.marginLeft, y = itemTitle.bottom  + it.marginTop) }
        reply.let { it.layout(x = it.marginRight,y = avatar.top + it.marginTop, fromRight = true) }
    }

    protected val View.measuredWidthWithMargins: Int
        get() {
            return measuredWidth + marginLeft + marginRight
        }

    protected val View.measuredHeightWithMargins: Int
        get() {
            return measuredHeight + marginTop + marginBottom
        }

    protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false){
        if (!fromRight){
            layout(x,y,x + measuredWidth,y + measuredHeight)
        } else {
            layout(this@CustomLayout.measuredWidth -x - measuredWidth, y)
        }
    }

    protected fun View.autoMeasure() {
        measure(
            this.defaultWidthMeasureSpec(parentView = this@CustomLayout),
            this.defaultHeightMeasureSpec(parentView = this@CustomLayout)
        )
    }

    protected fun View.defaultWidthMeasureSpec(parentView: ViewGroup): Int {
        return when (layoutParams.width) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    protected fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
        return when (layoutParams.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> ViewGroup.LayoutParams.WRAP_CONTENT.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    protected val Int.dp: Int get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    protected fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    protected fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

}