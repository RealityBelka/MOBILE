package ru.gozerov.core.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import ru.gozerov.core.R

class WaveformView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paint = Paint().apply {
        color = getContext().getColor(R.color.primary)
    }
    private var amplitudes = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()

    private var radius = 10f
    private var w = 22f
    private var d = 20f

    private var sw = 0f
    private var sh = 200f

    private var maxSpikes = 0

    init {

        sw = resources.displayMetrics.widthPixels.toFloat()

        maxSpikes = (sw / (w + d)).toInt()
    }

    fun addAmplitude(amp: Float) {
        val norm = (amp.toInt() / 20).coerceIn(25..200).toFloat()
        amplitudes.add(norm)

        spikes.clear()
        val amps = amplitudes.takeLast(maxSpikes)
        for (i in amps.indices) {
            val left = sw - i * (w + d)
            val top = sh / 2 - amps[i] / 2
            val right = left + w
            val bottom = top + amps[i]
            spikes.add(RectF(left, top, right, bottom))
        }

        invalidate()
    }

    fun clear(): ArrayList<Float> {
        val amps = amplitudes
        amplitudes.clear()
        spikes.clear()
        invalidate()

        return amps
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        spikes.forEach {
            canvas.drawRoundRect(it, radius, radius, paint)
        }
    }

}