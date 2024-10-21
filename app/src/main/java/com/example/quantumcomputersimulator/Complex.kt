package com.example.quantumcomputersimulator

import java.io.Serializable
import kotlin.math.*

data class Complex(val real: Double, val imaginary: Double) : Serializable {
    operator fun plus(other: Complex) = Complex(real + other.real, imaginary + other.imaginary)
    operator fun minus(other: Complex) = Complex(real - other.real, imaginary - other.imaginary)
    operator fun times(other: Complex) =
        Complex(real * other.real - imaginary * other.imaginary,
            real * other.imaginary + imaginary * other.real)
    operator fun times(scalar: Double) = Complex(real * scalar, imaginary * scalar)
    operator fun div(other: Complex): Complex {
        val denominator = other.real * other.real + other.imaginary * other.imaginary
        return Complex(
            (real * other.real + imaginary * other.imaginary) / denominator,
            (imaginary * other.real - real * other.imaginary) / denominator
        )
    }
    operator fun div(scalar: Double) = Complex(real / scalar, imaginary / scalar)

    fun conjugate() = Complex(real, -imaginary)
    fun magnitude() = sqrt(real * real + imaginary * imaginary)
    fun magnitudeSquared() = real * real + imaginary * imaginary
    fun phase() = atan2(imaginary, real)

    fun toRoundedString(): String {
        val realPart = "%.2f".format(real)
        val imagPart = "%.2f".format(abs(imaginary))
        return when {
            imaginary == 0.0 -> realPart
            real == 0.0 -> "${if (imaginary > 0) "" else "-"}${imagPart}i"
            imaginary > 0 -> "$realPart + ${imagPart}i"
            else -> "$realPart - ${imagPart}i"
        }
    }

    override fun toString(): String {
        return when {
            imaginary == 0.0 -> real.toString()
            real == 0.0 -> "${imaginary}i"
            imaginary > 0 -> "$real + ${imaginary}i"
            else -> "$real - ${abs(imaginary)}i"
        }
    }

    companion object {
        val ZERO = Complex(0.0, 0.0)
        val ONE = Complex(1.0, 0.0)
        val I = Complex(0.0, 1.0)
    }
}

// Extension functions for Double to allow operations with Complex numbers
operator fun Double.plus(c: Complex) = Complex(this + c.real, c.imaginary)
operator fun Double.minus(c: Complex) = Complex(this - c.real, -c.imaginary)
operator fun Double.times(c: Complex) = Complex(this * c.real, this * c.imaginary)
operator fun Double.div(c: Complex): Complex {
    val denominator = c.real * c.real + c.imaginary * c.imaginary
    return Complex(
        (this * c.real) / denominator,
        (-this * c.imaginary) / denominator
    )
}