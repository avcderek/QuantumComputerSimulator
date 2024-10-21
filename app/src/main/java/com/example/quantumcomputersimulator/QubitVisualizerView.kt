package com.example.quantumcomputersimulator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class QubitVisualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.BLUE
    }

    private var theta = 0f
    private var phi = 0f

    fun updateState(newTheta: Float, newPhi: Float) {
        theta = newTheta
        phi = newPhi
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(centerX, centerY) - paint.strokeWidth

        // Draw the Bloch sphere
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw the state vector
        val x = radius * sin(theta) * cos(phi)
        val y = radius * sin(theta) * sin(phi)
        val z = radius * cos(theta)

        canvas.drawLine(centerX, centerY, centerX + x, centerY - z, paint.apply { color = Color.RED })
        canvas.drawLine(centerX, centerY, centerX + x, centerY + y, paint.apply { color = Color.GREEN })
    }
}