package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimalListeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter
    private lateinit var db: AppDatabase
    private lateinit var animalDao: AnimalDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_liste)

        recyclerView = findViewById(R.id.animalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db = DatabaseProvider.getDatabase(this)
        animalDao = db.animalDao()

        loadAnimals()
    }

    private fun loadAnimals() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val animals = animalDao.getAllAnimals()

                // Mapper les animaux pour inclure les noms des parents
                val animalsWithParents = animals.map { animal ->
                    val parent1Name = animal.identificationparent1?.let { animalDao.getParentName(it) } ?: "Inconnu"
                    val parent2Name = animal.identificationparent2?.let { animalDao.getParentName(it) } ?: "Inconnu"

                    AnimalWithParents(
                        id = animal.id,
                        nom = animal.nom,
                        especeid = animal.especeid,
                        sexe = animal.sexe,
                        datedenaissance = animal.datedenaissance,
                        vivant = animal.vivant,
                        identification = animal.identification,
                        parent1Name = parent1Name,
                        parent2Name = parent2Name
                    )
                }

                withContext(Dispatchers.Main) {
                    adapter = AnimalAdapter(animalsWithParents)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e("AnimalListeActivity", "Erreur lors du chargement des animaux : ${e.message}")
            }
        }
    }

    data class AnimalWithParents(
        val id: Int,
        val nom: String,
        val especeid: Int,
        val sexe: String,
        val datedenaissance: String,
        val vivant: Boolean,
        val identification: String,
        val parent1Name: String,
        val parent2Name: String
    )
}
