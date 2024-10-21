package com.example.quantumcomputersimulator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class BlochSphereActivity : AppCompatActivity() {
    private lateinit var blochSphereView: BlochSphereView
    private lateinit var tvState: TextView
    private lateinit var btnH: Button
    private lateinit var btnX: Button
    private lateinit var btnY: Button
    private lateinit var btnZ: Button
    private lateinit var btnMeasure: Button
    private lateinit var btnInfo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bloch_sphere)

        blochSphereView = findViewById(R.id.blochSphereView)
        tvState = findViewById(R.id.tvState)
        btnH = findViewById(R.id.btnH)
        btnX = findViewById(R.id.btnX)
        btnY = findViewById(R.id.btnY)
        btnZ = findViewById(R.id.btnZ)
        btnMeasure = findViewById(R.id.btnMeasure)
        btnInfo = findViewById(R.id.btnInfo)

        val alpha = intent.getSerializableExtra("alpha") as? Complex ?: Complex(1.0, 0.0)
        val beta = intent.getSerializableExtra("beta") as? Complex ?: Complex(0.0, 0.0)

        updateState(alpha, beta)
        setupButtons()
    }

    private fun setupButtons() {
        btnH.setOnClickListener { applyGate(HadamardGate(0)) }
        btnX.setOnClickListener { applyGate(PauliXGate(0)) }
        btnY.setOnClickListener { applyGate(PauliYGate(0)) }
        btnZ.setOnClickListener { applyGate(PauliZGate(0)) }
        btnMeasure.setOnClickListener { measureQubit() }
        btnInfo.setOnClickListener { showInfo() }
    }

    private fun applyGate(gate: QuantumGate) {
        val qubit = Qubit(blochSphereView.alpha, blochSphereView.beta)
        gate.apply(listOf(qubit))
        updateState(qubit.alpha, qubit.beta)
    }

    private fun measureQubit() {
        val qubit = Qubit(blochSphereView.alpha, blochSphereView.beta)
        val result = qubit.measure()
        updateState(qubit.alpha, qubit.beta)
        AlertDialog.Builder(this)
            .setTitle("Measurement Result")
            .setMessage("The qubit collapsed to |${if (result) "1" else "0"}⟩")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun updateState(alpha: Complex, beta: Complex) {
        blochSphereView.updateState(alpha, beta)
        tvState.text = "State: ${alpha.toRoundedString()}|0⟩ + ${beta.toRoundedString()}|1⟩"
    }

    private fun showInfo() {
        AlertDialog.Builder(this)
            .setTitle("Bloch Sphere")
            .setMessage("The Bloch sphere is a geometric representation of the pure state space of a qubit. " +
                    "The north pole represents |0⟩, the south pole |1⟩. Any point on the surface represents a pure qubit state.")
            .setPositiveButton("OK", null)
            .show()
    }
}