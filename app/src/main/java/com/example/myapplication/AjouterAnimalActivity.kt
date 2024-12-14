package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.*


class AjouterAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_animal)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val speciesSpinner = findViewById<Spinner>(R.id.speciesSpinner) // Spinner pour afficher les espèces
        val sexSpinner = findViewById<Spinner>(R.id.sexSpinner) // Spinner pour le sexe
        val birthDateInput = findViewById<EditText>(R.id.birthDateInput)
        val identificationInput = findViewById<EditText>(R.id.identificationInput)
        val parent1Input = findViewById<EditText>(R.id.parent1Input)
        val parent2Input = findViewById<EditText>(R.id.parent2Input)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val db = DatabaseProvider.getDatabase(this)
        val speciesDao = db.typeEspeceDao()
        val animalDao = db.animalDao()

        // Charger les espèces dans le Spinner
        CoroutineScope(Dispatchers.IO).launch {
            val speciesList = speciesDao.getAllTypeEspeces().map { it.nom } // Récupère les noms des espèces
            runOnUiThread {
                val adapter = ArrayAdapter(this@AjouterAnimalActivity, android.R.layout.simple_spinner_item, speciesList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                speciesSpinner.adapter = adapter
            }
        }

        // Configurer le Spinner pour le sexe
        val sexAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("M", "F"))
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val species = speciesSpinner.selectedItem?.toString()?.trim()
            val sex = sexSpinner.selectedItem?.toString()?.trim()
            val birthDate = birthDateInput.text.toString().trim()
            val identification = identificationInput.text.toString().trim()
            val parent1 = parent1Input.text.toString().toIntOrNull()
            val parent2 = parent2Input.text.toString().toIntOrNull()

            if (name.isNotEmpty() && species != null && sex != null && birthDate.isNotEmpty() && identification.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val speciesId = speciesDao.getAllTypeEspeces().find { it.nom == species }?.id
                        if (speciesId != null) {
                            animalDao.insertAnimal(
                                Animal(
                                    nom = name,
                                    espece_id = speciesId,
                                    sexe = sex,
                                    date_de_naissance = birthDate,
                                    vivant = true,
                                    identification_parent1 = parent1,
                                    identification_parent2 = parent2,
                                    identification = identification
                                )
                            )
                            runOnUiThread {
                                Toast.makeText(this@AjouterAnimalActivity, "Animal ajouté avec succès", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@AjouterAnimalActivity, "Erreur : espèce introuvable", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@AjouterAnimalActivity, "Erreur lors de l'ajout de l'animal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs correctement", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
