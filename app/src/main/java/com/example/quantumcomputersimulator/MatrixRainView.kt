package com.example.quantumcomputersimulator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import kotlin.random.Random

class MatrixRainView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val random = Random.Default
    private val chars = "01"
    private val streams = mutableListOf<CharStream>()

    init {
        paint.color = Color.GREEN
        paint.textSize = 30f
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofInt(0, 1)
        animator.duration = 50 // Adjust for desired speed
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
            invalidate() // This will call onDraw
        }
        animator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        streams.clear()
        val numStreams = w / 30
        for (i in 0 until numStreams) {
            streams.add(CharStream(i * 30f, random.nextInt(h).toFloat()))
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        streams.forEach { stream ->
            stream.draw(canvas)
        }
    }

    inner class CharStream(private var x: Float, private var y: Float) {
        private val length = random.nextInt(5, 15)
        private val chars = CharArray(length)
        private val speeds = FloatArray(length) { random.nextFloat() * 5 + 5 }

        init {
            for (i in chars.indices) {
                chars[i] = this@MatrixRainView.chars[random.nextInt(this@MatrixRainView.chars.length)]
            }
        }

        fun draw(canvas: Canvas) {
            for (i in chars.indices) {
                val alpha = 255 - (255f / length * i).toInt()
                paint.alpha = alpha
                canvas.drawText(chars[i].toString(), x, y - 30f * i, paint)
            }
            y += speeds[0]
            if (y > height + length * 30) {
                y = -30f * length
                x = random.nextInt(width).toFloat()
            }
            for (i in chars.indices) {
                if (random.nextFloat() < 0.1f) {
                    chars[i] = this@MatrixRainView.chars[random.nextInt(this@MatrixRainView.chars.length)]
                }
            }
        }
    }
}