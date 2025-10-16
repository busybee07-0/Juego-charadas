package com.JavierF.charadas

import org.junit.Assert.assertFalse
import org.junit.Test

class WordsRepositoryTest {

    @Test
    fun no_repite_hasta_agotar_categoria() {
        val repo = WordsRepository()
        val categoria = "Animales"
        repo.resetCategory(categoria)

        val vistos = mutableSetOf<String>()
        repeat(10) { // cambia 10 si tu lista tiene más palabras
            val palabra = repo.nextWord()
            assertFalse("La palabra $palabra se repitió antes de agotar la lista", vistos.contains(palabra))
            vistos.add(palabra)
        }
    }
}
