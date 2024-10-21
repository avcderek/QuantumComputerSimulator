package com.example.quantumcomputersimulator

import java.io.Serializable
import kotlin.random.Random

data class Qubit(var alpha: Complex, var beta: Complex) : Serializable {
    fun applyGate(gate: Array<Array<Complex>>) {
        val newAlpha = gate[0][0] * alpha + gate[0][1] * beta
        val newBeta = gate[1][0] * alpha + gate[1][1] * beta
        alpha = newAlpha
        beta = newBeta
    }

    fun measure(): Boolean {
        val probability0 = probabilityOf0()
        val result = Random.nextDouble() < probability0
        if (result) {
            alpha = Complex(1.0, 0.0)
            beta = Complex(0.0, 0.0)
        } else {
            alpha = Complex(0.0, 0.0)
            beta = Complex(1.0, 0.0)
        }
        return result
    }

    fun probabilityOf0(): Double = alpha.magnitudeSquared()

    fun probabilityOf1(): Double = beta.magnitudeSquared()

    fun isApproximatelyEqual(other: Qubit, epsilon: Double = 1e-6): Boolean {
        return (alpha - other.alpha).magnitude() < epsilon &&
                (beta - other.beta).magnitude() < epsilon
    }

    override fun toString(): String {
        return "α = ${alpha.toRoundedString()}, β = ${beta.toRoundedString()}"
    }
}