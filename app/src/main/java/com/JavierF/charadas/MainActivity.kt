package com.JavierF.charadas  // ← cámbialo si tu package es distinto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val categorias = listOf("Animales", "Películas", "Profesiones")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spCategory   = findViewById<Spinner>(R.id.spCategory)
        val etSeconds    = findViewById<EditText>(R.id.etSeconds)
        val etTeamA      = findViewById<EditText>(R.id.etTeamA)
        val etTeamB      = findViewById<EditText>(R.id.etTeamB)
        val btnStart     = findViewById<Button>(R.id.btnStart)
        val btnResetMain = findViewById<Button>(R.id.btnResetMain)

        // Spinner de categorías
        spCategory.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )

        // Cargar nombres guardados y decidir si mostrar el botón de reset
        val store = ScoreStore(this)
        val (savedA, savedB) = store.getTeamNames()
        etTeamA.setText(savedA.ifBlank { "Equipo A" })
        etTeamB.setText(savedB.ifBlank { "Equipo B" })

        val (totA, totB) = store.getTotals()
        btnResetMain.visibility = if (totA > 0 || totB > 0) View.VISIBLE else View.GONE

        // Reiniciar marcador Y nombres + feedback visual
        btnResetMain.setOnClickListener {
            store.resetAll() // puntajes y nombres a valores por defecto
            etTeamA.setText("Equipo A")
            etTeamB.setText("Equipo B")
            etSeconds.setText("60")
            spCategory.setSelection(0)
            btnResetMain.visibility = View.GONE
            Toast.makeText(this, "Marcador y nombres reiniciados", Toast.LENGTH_SHORT).show()
        }

        // Iniciar flujo: empieza siempre Equipo A, 1ª del par
        btnStart.setOnClickListener {
            val secs = etSeconds.text.toString().toIntOrNull() ?: 60
            val category = spCategory.selectedItem?.toString() ?: categorias.first()
            val teamA = etTeamA.text.toString().ifBlank { "Equipo A" }
            val teamB = etTeamB.text.toString().ifBlank { "Equipo B" }

            // Guardar nombres para mantenerlos al volver al menú
            store.saveTeamNames(teamA, teamB)

            val i = Intent(this, GameActivity::class.java).apply {
                putExtra("category", category)
                putExtra("seconds", secs)
                putExtra("teamA", teamA)
                putExtra("teamB", teamB)
                putExtra("teamAPlaying", true)     // empieza Equipo A
                putExtra("isSecondOfPair", false)  // 1ª ronda del par (A)
            }
            startActivity(i)
        }
    }
}
