package com.JavierF.charadas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class ResultActivity : ComponentActivity() {

    private lateinit var store: ScoreStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        store = ScoreStore(this)

        val roundScore = intent.getIntExtra("roundScore", 0)
        val teamAPlaying = intent.getBooleanExtra("teamAPlaying", true)

        // Nombres guardados (con fallback seguro)
        val (savedA, savedB) = store.getTeamNames()
        val nameA = intent.getStringExtra("teamA")?.ifBlank { savedA } ?: savedA
        val nameB = intent.getStringExtra("teamB")?.ifBlank { savedB } ?: savedB

        val (totA, totB) = store.getTotals()

        // UI
        val tvWinner = findViewById<TextView>(R.id.tvWinner)
        val tvSummary = findViewById<TextView>(R.id.tvSummary)
        val tvRowTeamA = findViewById<TextView>(R.id.tvRowTeamA)
        val tvRowTeamB = findViewById<TextView>(R.id.tvRowTeamB)
        val tvRowPointsA = findViewById<TextView>(R.id.tvRowPointsA)
        val tvRowPointsB = findViewById<TextView>(R.id.tvRowPointsB)
        val btnMenu = findViewById<Button>(R.id.btnMenu)
        val btnReset = findViewById<Button>(R.id.btnReset)

        // Resumen de la última ronda
        val quien = if (teamAPlaying) nameA else nameB
        tvSummary.text = "¡Tiempo! $quien hizo $roundScore punto(s)"

        // “Tabla”
        tvRowTeamA.text = nameA
        tvRowTeamB.text = nameB
        tvRowPointsA.text = totA.toString()
        tvRowPointsB.text = totB.toString()

        // Ganador (encabezado)
        when {
            totA > totB -> {
                tvWinner.text = "$nameA es el GANADOR"
                tvWinner.setTextColor(Color.parseColor("#2E7D32"))
            }
            totB > totA -> {
                tvWinner.text = "$nameB es el GANADOR"
                tvWinner.setTextColor(Color.parseColor("#2E7D32"))
            }
            else -> {
                tvWinner.text = "¡EMPATE!"
                tvWinner.setTextColor(Color.parseColor("#F57C00"))
            }
        }

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnReset.setOnClickListener {
            store.resetTotals()
            tvRowPointsA.text = "0"
            tvRowPointsB.text = "0"
            tvWinner.text = "Marcador reiniciado"
            tvWinner.setTextColor(Color.DKGRAY)
            tvSummary.text = ""
        }
    }
}
