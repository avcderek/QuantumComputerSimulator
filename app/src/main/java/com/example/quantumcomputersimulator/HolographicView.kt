package com.example.quantumcomputersimulator

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import kotlin.math.min

class HolographicView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shader: Shader
    private var animation: Animation? = null

    init {
        val colors = intArrayOf(
            Color.argb(0, 0, 255, 0),
            Color.argb(100, 0, 255, 0),
            Color.argb(0, 0, 255, 0)
        )
        shader = LinearGradient(0f, 0f, 100f, 0f, colors, null, Shader.TileMode.MIRROR)
        paint.shader = shader

        startAnimation()
    }

    private fun startAnimation() {
        animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        ).apply {
            duration = 1500
            repeatCount = Animation.INFINITE
            repeatMode = Animation.RESTART
            interpolator = LinearInterpolator()
        }
        startAnimation(animation)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2f
        val lineHeight = min(height * 0.8f, 100f)
        canvas.drawLine(0f, centerY, width.toFloat(), centerY, paint)
        canvas.drawRect(0f, centerY - lineHeight / 2, width.toFloat(), centerY + lineHeight / 2, paint)
    }
}