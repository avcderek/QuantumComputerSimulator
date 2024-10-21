package com.example.quantumcomputersimulator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
import kotlin.random.Random

class QuantumPuzzleActivity : AppCompatActivity() {
    private lateinit var tvCurrentState: TextView
    private lateinit var tvTargetState: TextView
    private lateinit var tvMoves: TextView
    private lateinit var btnH: Button
    private lateinit var btnX: Button
    private lateinit var btnY: Button
    private lateinit var btnZ: Button
    private lateinit var btnReset: Button
    private lateinit var tvInstructions: TextView

    private lateinit var currentQubit: Qubit
    private lateinit var targetQubit: Qubit
    private var moves = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quantum_puzzle)

        tvCurrentState = findViewById(R.id.tvCurrentState)
        tvTargetState = findViewById(R.id.tvTargetState)
        tvMoves = findViewById(R.id.tvMoves)
        btnH = findViewById(R.id.btnH)
        btnX = findViewById(R.id.btnX)
        btnY = findViewById(R.id.btnY)
        btnZ = findViewById(R.id.btnZ)
        btnReset = findViewById(R.id.btnReset)
        tvInstructions = findViewById(R.id.tvInstructions)

        setupGame()
        setupButtons()
        setupInstructions()
    }

    private fun setupGame() {
        currentQubit = Qubit(Complex(1.0, 0.0), Complex(0.0, 0.0))
        targetQubit = generateRandomQubit()
        moves = 0
        updateUI()
    }

    private fun setupButtons() {
        btnH.setOnClickListener { applyGate(HadamardGate(0)) }
        btnX.setOnClickListener { applyGate(PauliXGate(0)) }
        btnY.setOnClickListener { applyGate(PauliYGate(0)) }
        btnZ.setOnClickListener { applyGate(PauliZGate(0)) }
        btnReset.setOnClickListener { setupGame() }
    }

    private fun setupInstructions() {
        val instructions = """
            Instructions:
            1. The goal is to match the Current state with the Target state.
            2. Use the quantum gates (H, X, Y, Z) to manipulate the current qubit state.
            3. H: Hadamard gate - Creates superposition
            4. X: Pauli-X gate - Flips the qubit (NOT gate)
            5. Y: Pauli-Y gate - Rotation around Y-axis
            6. Z: Pauli-Z gate - Phase flip
            7. Try to reach the target state in as few moves as possible!
        """.trimIndent()
        tvInstructions.text = instructions
    }

    private fun applyGate(gate: QuantumGate) {
        gate.apply(listOf(currentQubit))
        moves++
        updateUI()
        checkWinCondition()
    }

    private fun updateUI() {
        tvCurrentState.text = "Current: $currentQubit"
        tvTargetState.text = "Target: $targetQubit"
        tvMoves.text = "Moves: $moves"
    }

    private fun checkWinCondition() {
        if (currentQubit.isApproximatelyEqual(targetQubit)) {
            AlertDialog.Builder(this)
                .setTitle("Puzzle Solved!")
                .setMessage("You matched the target state in $moves moves.")
                .setPositiveButton("New Puzzle") { _, _ -> setupGame() }
                .show()
        }
    }

    private fun generateRandomQubit(): Qubit {
        val theta = Random.nextDouble() * PI
        val phi = Random.nextDouble() * 2 * PI
        return Qubit(
            Complex(cos(theta/2), 0.0),
            Complex(sin(theta/2) * cos(phi), sin(theta/2) * sin(phi))
        )
    }
}