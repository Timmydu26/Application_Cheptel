package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimalListeActivity : AppCompatActivity() {

    private lateinit var animalRecyclerView: RecyclerView
    private lateinit var animalAdapter: AnimalAdapter
    private lateinit var speciesSpinner: Spinner
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_liste)

        animalRecyclerView = findViewById(R.id.animalRecyclerView)
        speciesSpinner = findViewById(R.id.speciesSpinner)
        backButton = findViewById(R.id.backButton)

        animalAdapter = AnimalAdapter(emptyList())
        animalRecyclerView.layoutManager = LinearLayoutManager(this)
        animalRecyclerView.adapter = animalAdapter

        val db = DatabaseProvider.getDatabase(this)
        val animalDao = db.animalDao()
        val especeDao = db.typeEspeceDao()

        // Charger les espèces dans le Spinner et configurer le filtrage
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val speciesList = especeDao.getAllTypeEspeces().map { it.nom }
                val spinnerOptions = listOf("Tout") + speciesList

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(
                        this@AnimalListeActivity,
                        android.R.layout.simple_spinner_item,
                        spinnerOptions
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    speciesSpinner.adapter = adapter

                    // Charger tous les animaux initialement
                    loadAnimals(animalDao, species = null)

                    // Filtrer les animaux lors de la sélection dans le Spinner
                    speciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                            val selectedSpecies = if (position == 0) null else spinnerOptions[position]
                            loadAnimals(animalDao, selectedSpecies)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            loadAnimals(animalDao, species = null)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AnimalListeActivity", "Erreur lors du chargement des espèces : ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AnimalListeActivity, "Erreur lors du chargement des espèces.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Bouton Retour
        backButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Charge les animaux en fonction de l'espèce sélectionnée (ou tous si species == null).
     */
    private fun loadAnimals(animalDao: AnimalDao, species: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val animals = if (species.isNullOrEmpty()) {
                    animalDao.getAllAnimals()
                } else {
                    val filteredAnimals = animalDao.getAllAnimals().filter { it.especeid == getSpeciesIdByName(species) }
                    filteredAnimals
                }

                withContext(Dispatchers.Main) {
                    if (animals.isEmpty()) {
                        Toast.makeText(this@AnimalListeActivity, "Aucun animal trouvé.", Toast.LENGTH_SHORT).show()
                    }
                    animalAdapter.updateData(animals)
                }
            } catch (e: Exception) {
                Log.e("AnimalListeActivity", "Erreur lors du chargement des animaux : ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AnimalListeActivity, "Erreur lors du chargement des animaux.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Récupère l'ID d'une espèce par son nom.
     */
    private suspend fun getSpeciesIdByName(speciesName: String): Int? {
        val db = DatabaseProvider.getDatabase(this)
        val speciesDao = db.typeEspeceDao()
        return speciesDao.getAllTypeEspeces().find { it.nom == speciesName }?.id
    }
}
