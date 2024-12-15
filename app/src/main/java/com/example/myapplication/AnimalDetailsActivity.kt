package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimalDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_details)

        val animalNameTextView = findViewById<TextView>(R.id.animalNameTextView)
        val speciesTextView = findViewById<TextView>(R.id.speciesTextView)
        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        val birthDateTextView = findViewById<TextView>(R.id.birthDateTextView)
        val identificationTextView = findViewById<TextView>(R.id.identificationTextView)
        val parent1TextView = findViewById<TextView>(R.id.parent1TextView)
        val parent2TextView = findViewById<TextView>(R.id.parent2TextView)
        val healthDataTextView = findViewById<TextView>(R.id.healthDataTextView)

        val animalId = intent.getIntExtra("animal_id", -1)
        if (animalId == -1) {
            Toast.makeText(this, "Aucun animal sélectionné", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = DatabaseProvider.getDatabase(this)
        val animalDao = db.animalDao()
        val speciesDao = db.typeEspeceDao()
        val healthDataDao = db.donneeSanteDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val animal = animalDao.getAnimalById(animalId)
                if (animal == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AnimalDetailsActivity, "Animal introuvable", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    return@launch
                }

                val speciesName = speciesDao.getAllTypeEspeces().find { it.id == animal.especeid }?.nom ?: "Inconnu"
                val parent1Name = animal.identificationparent1?.let { id -> animalDao.getAnimalById(id)?.nom } ?: "Inconnue"
                val parent2Name = animal.identificationparent2?.let { id -> animalDao.getAnimalById(id)?.nom } ?: "Inconnu"

                val healthData = healthDataDao.getDonneesSanteForAnimal(animal.id).joinToString("\n") { data ->
                    "Date : ${data.dateRealisation}\nType d'acte : ${data.typeActeId}\nDétails : ${data.details}\n"
                }

                withContext(Dispatchers.Main) {
                    animalNameTextView.text = "Nom : ${animal.nom}"
                    speciesTextView.text = "Espèce : $speciesName"
                    statusTextView.text = if (animal.vivant) "Statut : Vivant" else "Statut : Mort"
                    birthDateTextView.text = "Date de naissance : ${animal.datedenaissance}"
                    identificationTextView.text = "Identification : ${animal.identification}"
                    parent1TextView.text = "Mère : $parent1Name"
                    parent2TextView.text = "Père : $parent2Name"
                    healthDataTextView.text = "Données santé :\n$healthData"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AnimalDetailsActivity,
                        "Erreur lors du chargement des détails de l'animal : ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
