package com.JavierF.charadas

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class GameActivity : ComponentActivity() {

    private lateinit var repo: WordsRepository
    private lateinit var store: ScoreStore

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
    private var isSecondOfPair = false   // false = 1ª ronda (A), true = 2ª ronda (B)

    private var roundScore = 0
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        store = ScoreStore(this)

        intent.extras?.let {
            category = it.getString("category", category)
            seconds = it.getInt("seconds", seconds)
            teamA = it.getString("teamA", teamA)
            teamB = it.getString("teamB", teamB)
            teamAPlaying = it.getBoolean("teamAPlaying", true)
            isSecondOfPair = it.getBoolean("isSecondOfPair", false)
        }

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
                val s = (ms / 1000).toInt()
                tvTimer.text = s.toString()
                if (s in 1..3) flashBackground() // <- últimos 3s
            }
            override fun onFinish() {
                tvTimer.text = "0"
                endRound()
            }
        }.start()
    }

    private fun flashBackground() {
        val from = Color.TRANSPARENT
        val to = Color.parseColor("#55FF0000") // rojo translúcido
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), from, to, from)
        anim.duration = 500
        anim.addUpdateListener { valueAnimator ->
            window.decorView.setBackgroundColor(valueAnimator.animatedValue as Int)
        }
        anim.start()
    }

    private fun endRound() {
        timer?.cancel()
        // Sumar al equipo que jugó
        store.addTo(teamAPlaying, roundScore)

        if (!isSecondOfPair) {
            // Era la 1ª del par (por ejemplo A). Mostramos pantalla para B y arrancamos en 5s.
            val i = Intent(this, SwitchTeamActivity::class.java).apply {
                putExtra("category", category)
                putExtra("seconds", seconds)
                putExtra("teamA", teamA)
                putExtra("teamB", teamB)
                putExtra("nextIsTeamA", !teamAPlaying) // alternamos
                putExtra("isSecondOfPair", true)       // la próxima será la 2ª del par
            }
            startActivity(i)
            finish()
        } else {
            // Era la 2ª del par -> vamos a resultados
            val i = Intent(this, ResultActivity::class.java).apply {
                putExtra("roundScore", roundScore)
                putExtra("teamAPlaying", teamAPlaying)
                putExtra("teamA", teamA)
                putExtra("teamB", teamB)
            }
            startActivity(i)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }
}
