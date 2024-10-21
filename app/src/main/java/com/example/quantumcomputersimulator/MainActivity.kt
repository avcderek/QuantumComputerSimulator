package com.example.quantumcomputersimulator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private var qubits: MutableList<Qubit> = mutableListOf()
    private var circuit: MutableList<QuantumGate> = mutableListOf()
    private lateinit var spinnerQubitCount: Spinner
    private lateinit var layoutQubitStates: LinearLayout
    private lateinit var layoutCircuit: LinearLayout
    private var previousQubits: List<Qubit> = listOf()
    private var previousCircuit: List<QuantumGate> = listOf()
    private var initialQubits: List<Qubit> = listOf()
    private var initialCircuit: List<QuantumGate> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        spinnerQubitCount = findViewById(R.id.spinnerQubitCount)
        layoutQubitStates = findViewById(R.id.layoutQubitStates)
        layoutCircuit = findViewById(R.id.layoutCircuit)

        setupQubitCountSpinner()
        setupButtonListeners()
        initializeQubits(1) // Start with 1 qubit by default
    }

    private fun setupButtonListeners() {
        setClickListener(R.id.btnAddGate) { showGateSelectionDialog() }
        setClickListener(R.id.btnRunCircuit) { runCircuit() }
        setClickListener(R.id.btnMeasure) { measureAllQubits() }
        setClickListener(R.id.btnViewBlochSphere) { openBlochSphereView() }
        setClickListener(R.id.btnQuantumPuzzle) { openQuantumPuzzle() }
        setClickListener(R.id.btnExplainQuantumConcepts) { showExplanationDialog() }
        setClickListener(R.id.btnUndo) { undoInitialization() }
    }

    private fun setClickListener(id: Int, action: () -> Unit) {
        findViewById<Button>(id)?.setOnClickListener { action() } ?:
        Log.e("MainActivity", "Button with id $id not found")
    }

    private fun showExplanationDialog() {
        val concepts = arrayOf("Qubit", "Superposition", "Quantum Gate", "Hadamard Gate",
            "Pauli Gates", "CNOT Gate", "Measurement", "Quantum Circuit")
        AlertDialog.Builder(this, R.style.MatrixAlertDialogTheme)
            .setTitle("Explain Quantum Concepts")
            .setItems(concepts) { _, which ->
                val explanation = when (which) {
                    0 -> QuantumExplainer.explainQubit()
                    1 -> QuantumExplainer.explainSuperposition()
                    2 -> QuantumExplainer.explainQuantumGate()
                    3 -> QuantumExplainer.explainHadamardGate()
                    4 -> QuantumExplainer.explainPauliGates()
                    5 -> QuantumExplainer.explainCNOTGate()
                    6 -> QuantumExplainer.explainMeasurement()
                    7 -> QuantumExplainer.explainQuantumCircuit()
                    else -> "No explanation available."
                }
                showExplanation(concepts[which], explanation)
            }
            .show()
    }

    private fun showExplanation(title: String, explanation: String) {
        AlertDialog.Builder(this, R.style.MatrixAlertDialogTheme)
            .setTitle(title)
            .setMessage(explanation)
            .setPositiveButton("Got it") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun openBlochSphereView() {
        if (qubits.isNotEmpty()) {
            val intent = Intent(this, BlochSphereActivity::class.java).apply {
                putExtra("alpha", qubits[0].alpha as java.io.Serializable)
                putExtra("beta", qubits[0].beta as java.io.Serializable)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No qubits initialized", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openQuantumPuzzle() {
        val intent = Intent(this, QuantumPuzzleActivity::class.java)
        startActivity(intent)
    }

    private fun addCNOTGate() {
        if (qubits.size >= 2) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_cnot_qubit_selection, null)
            val spinnerControl = dialogView.findViewById<Spinner>(R.id.spinnerControlQubit)
            val spinnerTarget = dialogView.findViewById<Spinner>(R.id.spinnerTargetQubit)

            val qubitOptions = qubits.indices.map { "Qubit $it" }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, qubitOptions)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerControl.adapter = adapter
            spinnerTarget.adapter = adapter

            AlertDialog.Builder(this, R.style.MatrixAlertDialogTheme)
                .setTitle("Select Control and Target Qubits")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val controlQubit = spinnerControl.selectedItemPosition
                    val targetQubit = spinnerTarget.selectedItemPosition
                    if (controlQubit != targetQubit) {
                        addGateToCircuit(CNOTGate(control = controlQubit, target = targetQubit))
                    } else {
                        Toast.makeText(this, "Control and target qubits must be different", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "CNOT gate requires at least 2 qubits", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addHolographicEffect() {
        val holographicContainer = layoutInflater.inflate(R.layout.layout_holographic_container, null)
        val holographicText = holographicContainer.findViewById<TextView>(R.id.holographicText)

        // Example of updating the holographic text
        holographicText.text = "Quantum State: |ψ⟩ = α|0⟩ + β|1⟩"

        // Add the holographic container to your main layout
        findViewById<ViewGroup>(R.id.mainContainer).addView(holographicContainer)
    }

    private fun addGateToCircuit(gate: QuantumGate) {
        circuit.add(gate)
        gate.apply(qubits)
        updateCircuitView()
        updateQubitStates()
        explainCurrentState()
    }

    private fun runCircuit() {
        if (qubits.isNotEmpty()) {
            circuit.forEach { gate ->
                gate.apply(qubits)
            }
            updateQubitStates()
            explainCurrentState()
        } else {
            Toast.makeText(this, "No qubits initialized", Toast.LENGTH_SHORT).show()
        }
    }

    private fun measureAllQubits() {
        qubits.forEach { it.measure() }
        updateQubitStates()
        explainCurrentState()
    }

    private fun explainCurrentState() {
        val explanation = StringBuilder()
        explanation.append("Current State Explanation:\n\n")

        qubits.forEachIndexed { index, qubit ->
            explanation.append("Qubit $index:\n")
            explanation.append("- State: ${qubit.toString()}\n")
            explanation.append("- Probability of measuring |0⟩: ${qubit.probabilityOf0()}\n")
            explanation.append("- Probability of measuring |1⟩: ${qubit.probabilityOf1()}\n\n")
        }

        if (circuit.isNotEmpty()) {
            explanation.append("Circuit:\n")
            circuit.forEach { gate ->
                explanation.append("- ${gate.toString()}\n")
            }
        }

        showExplanation("Current Quantum State", explanation.toString())
    }

    private fun updateQubitStates() {
        layoutQubitStates.removeAllViews()
        qubits.forEachIndexed { index, qubit ->
            val stateView = TextView(this).apply {
                text = "Qubit $index: ${qubit.toString()}"
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.matrix_light_green))
            }
            layoutQubitStates.addView(stateView)
        }
    }

    private fun updateCircuitView() {
        layoutCircuit.removeAllViews()
        circuit.forEach { gate ->
            val gateView = TextView(this).apply {
                text = gate.toString()
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.matrix_light_green))
            }
            layoutCircuit.addView(gateView)
        }
    }

    private fun showGateSelectionDialog() {
        val gates = arrayOf("Hadamard", "Pauli-X", "Pauli-Y", "Pauli-Z", "CNOT")
        AlertDialog.Builder(this, R.style.MatrixAlertDialogTheme)
            .setTitle("Select a gate")
            .setItems(gates) { _, which ->
                when (which) {
                    0 -> addGateToCircuit(HadamardGate(selectQubit()))
                    1 -> addGateToCircuit(PauliXGate(selectQubit()))
                    2 -> addGateToCircuit(PauliYGate(selectQubit()))
                    3 -> addGateToCircuit(PauliZGate(selectQubit()))
                    4 -> addCNOTGate()
                }
            }
            .show()
    }

    private fun selectQubit(): Int {
        return 0 // For simplicity, selecting the first qubit.
    }

    private fun setupQubitCountSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.qubit_counts,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerQubitCount.adapter = adapter
        }

        spinnerQubitCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val count = parent.getItemAtPosition(pos).toString().toInt()
                initializeQubits(count)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun initializeQubits(count: Int, initialState: QubitState = QubitState.ZERO) {
        qubits = MutableList(count) {
            when (initialState) {
                QubitState.ZERO -> Qubit(Complex(1.0, 0.0), Complex(0.0, 0.0))
                QubitState.ONE -> Qubit(Complex(0.0, 0.0), Complex(1.0, 0.0))
                QubitState.PLUS -> Qubit(Complex(1 / sqrt(2.0), 0.0), Complex(1 / sqrt(2.0), 0.0))
                QubitState.MINUS -> Qubit(Complex(1 / sqrt(2.0), 0.0), Complex(-1 / sqrt(2.0), 0.0))
            }
        }
        circuit = mutableListOf()
        initialQubits = qubits.toList()
        initialCircuit = circuit.toList()
        updateQubitStates()
        updateCircuitView()
        explainCurrentState()


        runOnUiThread {
            val animation = AlphaAnimation(0f, 1f).apply {
                duration = 500
                interpolator = DecelerateInterpolator()
            }
            layoutQubitStates.startAnimation(animation)
        }
    }

    private fun undoInitialization() {
        if (qubits != initialQubits || circuit != initialCircuit) {
            qubits = initialQubits.toMutableList()
            circuit = initialCircuit.toMutableList()
            updateQubitStates()
            updateCircuitView()
            explainCurrentState()
        } else {
            Toast.makeText(this, "No changes to undo", Toast.LENGTH_SHORT).show()
        }
    }

sealed class QubitState {
    object ZERO : QubitState()
    object ONE : QubitState()
    object PLUS : QubitState()
    object MINUS : QubitState()
}}