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

        val (nameA, nameB) = store.getTeamNames()
        val (totA, totB) = store.getTotals()

        val tvWinner = findViewById<TextView>(R.id.tvWinner)
        val tvSummary = findViewById<TextView>(R.id.tvSummary)
        val tvRowTeamA = findViewById<TextView>(R.id.tvRowTeamA)
        val tvRowTeamB = findViewById<TextView>(R.id.tvRowTeamB)
        val tvRowPointsA = findViewById<TextView>(R.id.tvRowPointsA)
        val tvRowPointsB = findViewById<TextView>(R.id.tvRowPointsB)
        val btnMenu = findViewById<Button>(R.id.btnMenu)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val quien = if (teamAPlaying) nameA else nameB
        tvSummary.text = "¡Tiempo! $quien hizo $roundScore punto(s)"

        tvRowTeamA.text = nameA
        tvRowTeamB.text = nameB
        tvRowPointsA.text = totA.toString()
        tvRowPointsB.text = totB.toString()

        when {
            totA > totB -> {
                tvWinner.text = "$nameA es el GANADOR"
                tvWinner.background = resources.getDrawable(R.drawable.winner_badge, theme)
                tvWinner.setTextColor(Color.WHITE)
            }
            totB > totA -> {
                tvWinner.text = "$nameB es el GANADOR"
                tvWinner.background = resources.getDrawable(R.drawable.winner_badge, theme)
                tvWinner.setTextColor(Color.WHITE)
            }
            else -> {
                tvWinner.text = "¡EMPATE!"
                // Badge ámbar improvisado sobre el mismo drawable
                tvWinner.background = resources.getDrawable(R.drawable.winner_badge, theme)
                tvWinner.background.setTint(getColor(R.color.warning))
                tvWinner.setTextColor(Color.WHITE)
            }
        }

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnReset.setOnClickListener {
            store.resetAll()
            tvRowPointsA.text = "0"
            tvRowPointsB.text = "0"
            tvWinner.text = "Marcador reiniciado"
            tvWinner.background = null
            tvWinner.setTextColor(Color.DKGRAY)
            tvSummary.text = ""
        }
    }
}
