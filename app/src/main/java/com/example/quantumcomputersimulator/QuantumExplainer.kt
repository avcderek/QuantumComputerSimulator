package com.example.quantumcomputersimulator

object QuantumExplainer {
    fun explainQubit(): String {
        return "A qubit is the fundamental unit of quantum information. Unlike a classical bit " +
                "which can be either 0 or 1, a qubit can be in a superposition of both states. " +
                "It's represented by two complex numbers α and β, where |α|² + |β|² = 1."
    }

    fun explainSuperposition(): String {
        return "Superposition is a principle of quantum mechanics where a quantum system can exist " +
                "in multiple states at the same time. For a qubit, this means it can be in a " +
                "combination of both 0 and 1 states simultaneously."
    }

    fun explainQuantumGate(): String {
        return "A quantum gate is an operation applied to qubits. It's similar to logic gates in " +
                "classical computing, but can create and manipulate quantum superpositions and " +
                "entanglement. Common gates include Hadamard (H), Pauli-X, Y, Z, and CNOT."
    }

    fun explainHadamardGate(): String {
        return "The Hadamard gate creates an equal superposition of the 0 and 1 states. It's often " +
                "used to initialize qubits for quantum algorithms. When applied to |0⟩, it produces " +
                "the state (|0⟩ + |1⟩)/√2."
    }

    fun explainPauliGates(): String {
        return "Pauli gates are single-qubit rotations:\n" +
                "- X gate: Flips the qubit (like classical NOT gate)\n" +
                "- Y gate: Rotates the qubit around the Y-axis of the Bloch sphere\n" +
                "- Z gate: Flips the phase of the qubit"
    }

    fun explainCNOTGate(): String {
        return "The CNOT (Controlled-NOT) gate is a two-qubit gate. It flips the second qubit " +
                "(target) if and only if the first qubit (control) is in the |1⟩ state. It's " +
                "crucial for creating entanglement between qubits."
    }

    fun explainMeasurement(): String {
        return "Measuring a qubit collapses its quantum state. The result will be either 0 or 1, " +
                "with probabilities determined by the qubit's state before measurement. This " +
                "process is inherently probabilistic and irreversible."
    }

    fun explainQuantumCircuit(): String {
        return "A quantum circuit is a sequence of quantum gates applied to a set of qubits. " +
                "It represents a quantum computation or algorithm. The circuit starts with qubit " +
                "initialization, applies gates, and usually ends with measurement."
    }
}