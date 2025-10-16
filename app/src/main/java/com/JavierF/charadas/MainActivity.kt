package com.JavierF.charadas

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val categorias = listOf("Animales", "Películas", "Profesiones")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usamos el XML que creaste
        setContentView(R.layout.activity_main)

        val spCategory = findViewById<Spinner>(R.id.spCategory)
        val etSeconds  = findViewById<EditText>(R.id.etSeconds)
        val etTeamA    = findViewById<EditText>(R.id.etTeamA)
        val etTeamB    = findViewById<EditText>(R.id.etTeamB)
        val rbTeamA    = findViewById<RadioButton>(R.id.rbTeamA)
        val btnStart   = findViewById<Button>(R.id.btnStart)

        // 👇 Importante: especificar el tipo <String> para evitar "Cannot infer type"
        spCategory.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categorias
        )

        btnStart.setOnClickListener {
            val secs = etSeconds.text.toString().toIntOrNull() ?: 60
            val category = spCategory.selectedItem?.toString() ?: categorias.first()
            val teamA = etTeamA.text.toString().ifBlank { "Equipo A" }
            val teamB = etTeamB.text.toString().ifBlank { "Equipo B" }
            val teamAPlaying = rbTeamA.isChecked

            val i = Intent(this, GameActivity::class.java).apply {
                putExtra("category", category)
                putExtra("seconds", secs)
                putExtra("teamA", teamA)
                putExtra("teamB", teamB)
                putExtra("teamAPlaying", teamAPlaying)
            }
            ScoreStore(this).saveTeamNames(teamA, teamB)
            startActivity(i)
        }
    }
}
