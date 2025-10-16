package com.JavierF.charadas

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class GameActivity : ComponentActivity() {

    private lateinit var repo: WordsRepository
    private lateinit var tvHeader: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvWord: TextView
    private lateinit var btnPass: Button
    private lateinit var btnCorrect: Button

    private var category = "Animales"
    private var seconds = 60
    private var teamA = "Equipo A"
    private var teamB = "Equipo B"
    private var teamAPlaying = true

    private var roundScore = 0
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // lee extras de la Fase 1
        intent.extras?.let {
            category = it.getString("category", "Animales")
            seconds = it.getInt("seconds", 60)
            teamA = it.getString("teamA", "Equipo A")
            teamB = it.getString("teamB", "Equipo B")
            teamAPlaying = it.getBoolean("teamAPlaying", true)
        }

        // refs UI
        tvHeader = findViewById(R.id.tvHeader)
        tvTimer  = findViewById(R.id.tvTimer)
        tvScore  = findViewById(R.id.tvScore)
        tvWord   = findViewById(R.id.tvWord)
        btnPass  = findViewById(R.id.btnPass)
        btnCorrect = findViewById(R.id.btnCorrect)

        tvHeader.text = (if (teamAPlaying) teamA else teamB) + " • " + category
        tvTimer.text = seconds.toString()
        tvScore.text = "Puntos: 0"

        repo = WordsRepository().also { it.resetCategory(category) }
        showNextWord()

        btnCorrect.setOnClickListener {
            roundScore++
            tvScore.text = "Puntos: $roundScore"
            showNextWord()
        }
        btnPass.setOnClickListener { showNextWord() }

        startTimer()
    }

    private fun showNextWord() {
        tvWord.text = repo.nextWord()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(ms: Long) {
                tvTimer.text = (ms / 1000).toString()
            }
            override fun onFinish() {
                tvTimer.text = "0"
                endRound()
            }
        }.start()
    }

    private fun endRound() {
        timer?.cancel()
        // De momento solo volvemos al menú; en FASE 3 iremos a ResultActivity
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel() // el profe pidió controlar el temporizador por ronda
    }
}
