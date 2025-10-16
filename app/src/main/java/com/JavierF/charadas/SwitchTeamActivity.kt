package com.JavierF.charadas

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.ComponentActivity

class SwitchTeamActivity : ComponentActivity() {

    private var secondsToStart = 5
    private var category = "Animales"
    private var seconds = 60
    private var teamA = "Equipo A"
    private var teamB = "Equipo B"
    private var nextIsTeamA = false
    private var isSecondOfPair = true

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_team)

        val tvNextTeam   = findViewById<TextView>(R.id.tvNextTeam)
        val tvCountdown  = findViewById<TextView>(R.id.tvCountdown)
        val tvCategory   = findViewById<TextView>(R.id.tvCategory)
        val tvRoundTime  = findViewById<TextView>(R.id.tvRoundTime)   // ← NUEVO

        intent.extras?.let {
            category = it.getString("category", category)
            seconds  = it.getInt("seconds", seconds)
            teamA    = it.getString("teamA", teamA)
            teamB    = it.getString("teamB", teamB)
            nextIsTeamA   = it.getBoolean("nextIsTeamA", false)
            isSecondOfPair = it.getBoolean("isSecondOfPair", true)
        }

        val nextTeamName = if (nextIsTeamA) teamA else teamB
        tvNextTeam.text = "Siguiente: $nextTeamName"
        tvCategory.text = "Categoría: $category"
        tvRoundTime.text = "Ronda de ${seconds} s"   // ← NUEVO
        tvCountdown.text = "Comienza en $secondsToStart..."

        timer = object : CountDownTimer(secondsToStart * 1000L, 1000L) {
            override fun onTick(ms: Long) {
                val s = (ms / 1000).toInt()
                tvCountdown.text = "Comienza en $s..."
            }
            override fun onFinish() {
                startNextRound()
            }
        }.start()
    }

    private fun startNextRound() {
        val i = Intent(this, GameActivity::class.java).apply {
            putExtra("category", category)
            putExtra("seconds", seconds)
            putExtra("teamA", teamA)
            putExtra("teamB", teamB)
            putExtra("teamAPlaying", nextIsTeamA)
            putExtra("isSecondOfPair", isSecondOfPair)
        }
        startActivity(i)
        finish()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }
}

