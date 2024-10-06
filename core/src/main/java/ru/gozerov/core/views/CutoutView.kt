package ru.gozerov.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CutoutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val cutoutPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private val borderPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 4f * resources.displayMetrics.density
        isAntiAlias = true
    }

    private val ovalRect = RectF(0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        val ovalWidth = 340f * resources.displayMetrics.density
        val ovalHeight = 420f * resources.displayMetrics.density

        val left = (width - ovalWidth) / 2f
        val top = (height - ovalHeight) / 2f
        val right = left + ovalWidth
        val bottom = top + ovalHeight

        ovalRect.left = left
        ovalRect.top = top
        ovalRect.right = right
        ovalRect.bottom = bottom

        canvas.drawOval(ovalRect, cutoutPaint)

        canvas.drawOval(ovalRect, borderPaint)
    }

    fun setBorderColor(color: Int) {
        borderPaint.color = color
        invalidate()
    }

}