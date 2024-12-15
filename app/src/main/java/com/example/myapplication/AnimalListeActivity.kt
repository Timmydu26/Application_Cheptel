package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_liste)

        animalRecyclerView = findViewById(R.id.animalRecyclerView)
        speciesSpinner = findViewById(R.id.speciesSpinner)
        val backButton = findViewById<Button>(R.id.backButton)

        // Initialiser RecyclerView avec un adaptateur vide
        animalAdapter = AnimalAdapter(emptyList()) { animalWithSpecies ->
            // Lorsque l'élément est cliqué, démarrer AnimalDetailsActivity
            val intent = Intent(this, AnimalDetailsActivity::class.java)
            intent.putExtra("animalId", animalWithSpecies.animal.id)
            startActivity(intent)
        }
        animalRecyclerView.layoutManager = LinearLayoutManager(this)
        animalRecyclerView.adapter = animalAdapter

        // Charger la base de données
        val db = DatabaseProvider.getDatabase(this)
        val animalDao = db.animalDao()
        val especeDao = db.typeEspeceDao()

        // Charger les espèces dans le spinner
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val speciesList = especeDao.getAllTypeEspeces().map { it.nom }
                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(
                        this@AnimalListeActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Tout") + speciesList // Ajouter l'option "Tout"
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    speciesSpinner.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AnimalListeActivity, "Erreur lors du chargement des espèces", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Charger les animaux
        speciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSpecies = speciesSpinner.selectedItem.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val selectedSpeciesId: Int? = if (selectedSpecies == "Tout") {
                            null // Tout : Pas de filtre
                        } else {
                            especeDao.getAllTypeEspeces().find { it.nom == selectedSpecies }?.id
                        }
                        loadAnimals(animalDao, especeDao, selectedSpeciesId)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AnimalListeActivity, "Erreur lors du chargement des animaux", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Rien à faire ici
            }
        }

        // Bouton retour
        backButton.setOnClickListener {
            finish()
        }
    }

    private suspend fun loadAnimals(animalDao: AnimalDao, especeDao: TypeEspeceDao, speciesId: Int?) {
        try {
            val animals = if (speciesId == null) {
                animalDao.getAllAnimals()
            } else {
                animalDao.getAllAnimals().filter { it.especeid == speciesId }
            }

            val animalDetails = animals.map { animal ->
                val speciesName = especeDao.getAllTypeEspeces().find { it.id == animal.especeid }?.nom ?: "Inconnu"
                AnimalWithSpecies(
                    animal = animal,
                    especeNom = speciesName
                )
            }

            withContext(Dispatchers.Main) {
                animalAdapter.updateData(animalDetails)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AnimalListeActivity, "Erreur lors du chargement des animaux : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class AnimalWithSpecies(
        val animal: Animal,
        val especeNom: String
    )
}
