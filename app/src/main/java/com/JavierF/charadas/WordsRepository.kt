package com.JavierF.charadas

class WordsRepository {

    private val data: Map<String, List<String>> = mapOf(
        "Animales" to listOf("Perro","Gato","Elefante","Tiburón","Águila","Jirafa","Pingüino","Tortuga","Delfín","León"),
        "Películas" to listOf("El libro de la vida","Crepúsculo","Yo antes de ti","Up","Interestelar","Toy Story","Ponyo","El castillo ambulante","Coco","Big fish & begonia"),
        "Profesiones" to listOf("Médico","Ingeniero","Abogado","Panadero","Carpintero","Docente","Piloto","Chef","Fotógrafo","Arquitecto")
    )

    private var currentCategory = ""
    private var pool: MutableList<String> = mutableListOf()

    fun resetCategory(category: String) {
        currentCategory = category
        pool = (data[category] ?: emptyList()).toMutableList()
        pool.shuffle()
    }

    fun nextWord(): String {
        if (pool.isEmpty()) {
            pool = (data[currentCategory] ?: emptyList()).toMutableList()
            pool.shuffle()
        }
        return if (pool.isNotEmpty()) pool.removeAt(0) else "—"
    }
}
