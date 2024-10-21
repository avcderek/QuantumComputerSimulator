package com.example.quantumcomputersimulator

import kotlin.math.sqrt

interface QuantumGate {
    fun apply(qubits: List<Qubit>)
    fun explain(): String
    override fun toString(): String
}

class HadamardGate(private val qubit: Int) : QuantumGate {
    private val gate = arrayOf(
        arrayOf(Complex(1 / sqrt(2.0), 0.0), Complex(1 / sqrt(2.0), 0.0)),
        arrayOf(Complex(1 / sqrt(2.0), 0.0), Complex(-1 / sqrt(2.0), 0.0))
    )

    override fun apply(qubits: List<Qubit>) {
        qubits[qubit].applyGate(gate)
    }

    override fun explain() = "Hadamard gate on qubit $qubit: Creates superposition"
    override fun toString() = "H($qubit)"
}

class PauliXGate(private val qubit: Int) : QuantumGate {
    private val gate = arrayOf(
        arrayOf(Complex(0.0, 0.0), Complex(1.0, 0.0)),
        arrayOf(Complex(1.0, 0.0), Complex(0.0, 0.0))
    )

    override fun apply(qubits: List<Qubit>) {
        qubits[qubit].applyGate(gate)
    }

    override fun explain() = "Pauli-X gate on qubit $qubit: Flips the qubit state"
    override fun toString() = "X($qubit)"
}

class PauliYGate(private val qubit: Int) : QuantumGate {
    private val gate = arrayOf(
        arrayOf(Complex(0.0, 0.0), Complex(0.0, -1.0)),
        arrayOf(Complex(0.0, 1.0), Complex(0.0, 0.0))
    )

    override fun apply(qubits: List<Qubit>) {
        qubits[qubit].applyGate(gate)
    }

    override fun explain() = "Pauli-Y gate on qubit $qubit: Rotates the qubit state around Y-axis"
    override fun toString() = "Y($qubit)"
}

class PauliZGate(private val qubit: Int) : QuantumGate {
    private val gate = arrayOf(
        arrayOf(Complex(1.0, 0.0), Complex(0.0, 0.0)),
        arrayOf(Complex(0.0, 0.0), Complex(-1.0, 0.0))
    )

    override fun apply(qubits: List<Qubit>) {
        qubits[qubit].applyGate(gate)
    }

    override fun explain() = "Pauli-Z gate on qubit $qubit: Flips the phase of the qubit"
    override fun toString() = "Z($qubit)"
}

class CNOTGate(private val control: Int, private val target: Int) : QuantumGate {
    override fun apply(qubits: List<Qubit>) {
        val controlQubit = qubits[control]
        val targetQubit = qubits[target]

        val newAlpha = controlQubit.alpha * targetQubit.alpha + controlQubit.beta * targetQubit.beta
        val newBeta = controlQubit.alpha * targetQubit.beta + controlQubit.beta * targetQubit.alpha

        targetQubit.alpha = newAlpha
        targetQubit.beta = newBeta
    }

    override fun explain() = "CNOT gate with control qubit $control and target qubit $target: Flips target if control is |1⟩"
    override fun toString() = "CNOT($control, $target)"
}