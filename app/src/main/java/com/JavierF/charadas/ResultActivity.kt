package com.JavierF.charadas

import android.content.Intent
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
        val (teamA, teamB) = store.getTeamNames()
        val (totA, totB) = store.getTotals()

        val quien = if (teamAPlaying) teamA else teamB
        findViewById<TextView>(R.id.tvSummary).text =
            "¡Tiempo! $quien hizo $roundScore punto(s)"
        findViewById<TextView>(R.id.tvTotals).text =
            "Marcador global: $teamA $totA — $totB $teamB"

        findViewById<Button>(R.id.btnMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<Button>(R.id.btnReset).setOnClickListener {
            store.resetTotals()
            findViewById<TextView>(R.id.tvTotals).text =
                "Marcador global: $teamA 0 — 0 $teamB"
        }
    }
}
