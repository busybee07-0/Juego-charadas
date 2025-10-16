package com.JavierF.charadas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
    private lateinit var flashOverlay: View

    private var category = "Animales"
    private var seconds = 30
    private var teamA = "Equipo A"
    private var teamB = "Equipo B"
    private var teamAPlaying = true
    private var isSecondOfPair = false

    private var roundScore = 0
    private var timer: CountDownTimer? = null
    private var finishingOrPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        store = ScoreStore(this)

        intent.extras?.let {
            category = it.getString("category", category)
            seconds = it.getInt("seconds", seconds) // vendrá desde Main; default 30 allí
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
        flashOverlay = findViewById(R.id.flashOverlay)

        tvHeader.text = (if (teamAPlaying) teamA else teamB) + " • " + category
        tvTimer.text = seconds.toString()
        tvScore.text = "Puntos: 0"

        repo = WordsRepository().also { it.resetCategory(category) }
        showNextWord(pop = false)

        btnCorrect.setOnClickListener {
            roundScore++
            tvScore.text = "Puntos: $roundScore"
            flashOverlayColor(getColorCompat(R.color.success), 0.6f) // VERDE
            showNextWord(pop = true)
        }
        btnPass.setOnClickListener {
            flashOverlayColor(getColorCompat(R.color.danger), 0.6f)  // ROJO
            showNextWord(pop = true)
        }

        startTimer()
    }

    private fun showNextWord(pop: Boolean) {
        tvWord.text = repo.nextWord()
        if (pop) {
            tvWord.scaleX = 0.85f
            tvWord.scaleY = 0.85f
            tvWord.animate().scaleX(1f).scaleY(1f)
                .setDuration(180).setInterpolator(AccelerateDecelerateInterpolator()).start()
        }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(ms: Long) {
                val s = (ms / 1000).toInt()
                tvTimer.text = s.toString()
                if (s in 1..3) {
                    // PULSO AZUL (suave) usando el overlay, sin tocar decorView
                    flashOverlayColor(Color.parseColor("#332196F3"), 0.3f, inMs = 120, outMs = 180)
                }
            }
            override fun onFinish() {
                tvTimer.text = "0"
                endRound()
            }
        }.start()
    }

    /** ---------- ANIMACIONES CON OVERLAY ---------- **/
    private fun flashOverlayColor(color: Int, maxAlpha: Float, inMs: Long = 140, outMs: Long = 260) {
        if (finishingOrPaused) return
        try {
            flashOverlay.setBackgroundColor(color)
            flashOverlay.alpha = 0f
            flashOverlay.visibility = View.VISIBLE
            flashOverlay.animate().alpha(maxAlpha).setDuration(inMs).withEndAction {
                if (!finishingOrPaused) {
                    flashOverlay.animate().alpha(0f).setDuration(outMs).withEndAction {
                        flashOverlay.visibility = View.GONE
                    }.start()
                } else {
                    flashOverlay.visibility = View.GONE
                }
            }.start()
        } catch (_: Throwable) {
            // seguridad extra ante cualquier estado raro de la vista
            flashOverlay.visibility = View.GONE
        }
    }

    private fun getColorCompat(resId: Int): Int = resources.getColor(resId, theme)
    /** ---------- FIN ANIMACIONES ---------- **/

    private fun endRound() {
        timer?.cancel()
        store.addTo(teamAPlaying, roundScore)

        if (!isSecondOfPair) {
            val i = Intent(this, SwitchTeamActivity::class.java).apply {
                putExtra("category", category)
                putExtra("seconds", seconds)
                putExtra("teamA", teamA)
                putExtra("teamB", teamB)
                putExtra("nextIsTeamA", !teamAPlaying)
                putExtra("isSecondOfPair", true)
            }
            startActivity(i)
            finish()
        } else {
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
        finishingOrPaused = true
        super.onPause()
        timer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        finishingOrPaused = false
    }
}

