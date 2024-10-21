package com.example.quantumcomputersimulator

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class BlochSphereView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var rotationX = 0f
    private var rotationY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    var alpha = Complex(1.0, 0.0)
    var beta = Complex(0.0, 0.0)

    private val axes = listOf(
        "|0⟩" to PointF(0f, -1f),
        "|1⟩" to PointF(0f, 1f),
        "|+⟩" to PointF(1f, 0f),
        "|-⟩" to PointF(-1f, 0f),
        "|+i⟩" to PointF(0f, 0f)
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = minOf(w, h) / 2f * 0.8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw sphere
        paint.style = Paint.Style.STROKE
        paint.color = Color.GREEN
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw axes
        paint.color = Color.RED
        canvas.drawLine(centerX - radius, centerY, centerX + radius, centerY, paint)
        canvas.drawLine(centerX, centerY - radius, centerX, centerY + radius, paint)

        // Draw labels
        paint.textSize = 30f
        paint.style = Paint.Style.FILL
        axes.forEach { (label, point) ->
            val x = centerX + point.x * radius
            val y = centerY + point.y * radius
            canvas.drawText(label, x, y, paint)
        }

        // Draw state vector
        paint.color = Color.YELLOW
        val theta = 2 * acos(alpha.magnitude())
        val phi = beta.phase()
        val x = radius * sin(theta) * cos(phi)
        val y = radius * sin(theta) * sin(phi)
        val z = radius * cos(theta)

        // Apply rotation
        val rotatedX = x * cos(rotationY) - z * sin(rotationY)
        val rotatedY = y * cos(rotationX) + (x * sin(rotationY) + z * cos(rotationY)) * sin(rotationX)

        canvas.drawLine(centerX, centerY, centerX + rotatedX.toFloat(), centerY - rotatedY.toFloat(), paint)
        canvas.drawCircle(centerX + rotatedX.toFloat(), centerY - rotatedY.toFloat(), 10f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastTouchX
                val dy = event.y - lastTouchY
                rotationY += dx * 0.01f
                rotationX += dy * 0.01f
                lastTouchX = event.x
                lastTouchY = event.y
                invalidate()
            }
        }
        return true
    }

    fun updateState(newAlpha: Complex, newBeta: Complex) {
        alpha = newAlpha
        beta = newBeta
        invalidate()
    }
}