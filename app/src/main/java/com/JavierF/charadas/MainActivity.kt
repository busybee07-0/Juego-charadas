package com.JavierF.charadas

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

        spCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)

        val store = ScoreStore(this)
        val (savedA, savedB) = store.getTeamNames()
        etTeamA.setText(savedA.ifBlank { "Equipo A" })
        etTeamB.setText(savedB.ifBlank { "Equipo B" })

        val (totA, totB) = store.getTotals()
        btnResetMain.visibility = if (totA > 0 || totB > 0) View.VISIBLE else View.GONE

        btnResetMain.setOnClickListener {
            store.resetAll()
            etTeamA.setText("Equipo A")
            etTeamB.setText("Equipo B")
            etSeconds.setText("30")
            spCategory.setSelection(0)
            btnResetMain.visibility = View.GONE
            Toast.makeText(this, "Marcador y nombres reiniciados", Toast.LENGTH_SHORT).show()
        }

        btnStart.setOnClickListener {
            val secs = etSeconds.text.toString().toIntOrNull() ?: 30
            val category = spCategory.selectedItem?.toString() ?: categorias.first()
            val teamA = etTeamA.text.toString().ifBlank { "Equipo A" }
            val teamB = etTeamB.text.toString().ifBlank { "Equipo B" }

            store.saveTeamNames(teamA, teamB)

            val i = Intent(this, GameActivity::class.java).apply {
                putExtra("category", category)
                putExtra("seconds", secs)
                putExtra("teamA", teamA)
                putExtra("teamB", teamB)
                putExtra("teamAPlaying", true)
                putExtra("isSecondOfPair", false)
            }
            startActivity(i)
        }
    }
}
