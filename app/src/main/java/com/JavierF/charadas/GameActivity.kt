package com.JavierF.charadas   // <= SOLO una línea de package, igual a tu MainActivity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this).apply {
            textSize = 20f
            val cat  = intent.getStringExtra("category")
            val secs = intent.getIntExtra("seconds", 60)
            val teamA = intent.getStringExtra("teamA")
            val teamB = intent.getStringExtra("teamB")
            val whoA  = intent.getBooleanExtra("teamAPlaying", true)
            text = "OK ✅\nCategoría: $cat\nTiempo: $secs s\nEquipos: $teamA vs $teamB\nJuega: ${if (whoA) teamA else teamB}"
            setPadding(32, 32, 32, 32)
        }
        setContentView(tv)
    }
}
