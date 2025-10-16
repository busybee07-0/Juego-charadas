package com.JavierF.charadas

import android.content.Context

class ScoreStore(ctx: Context) {
    private val sp = ctx.getSharedPreferences("scores", Context.MODE_PRIVATE)

    fun saveTeamNames(a: String, b: String) {
        sp.edit().putString("teamA", a).putString("teamB", b).apply()
    }

    fun getTeamNames(): Pair<String, String> =
        sp.getString("teamA", "Equipo A")!! to sp.getString("teamB", "Equipo B")!!

    fun getTotals(): Pair<Int, Int> =
        sp.getInt("scoreA", 0) to sp.getInt("scoreB", 0)

    fun addTo(isTeamA: Boolean, delta: Int) {
        val (a, b) = getTotals()
        if (isTeamA) sp.edit().putInt("scoreA", a + delta).apply()
        else sp.edit().putInt("scoreB", b + delta).apply()
    }

    fun resetTotals() {
        sp.edit().putInt("scoreA", 0).putInt("scoreB", 0).apply()
    }
}
