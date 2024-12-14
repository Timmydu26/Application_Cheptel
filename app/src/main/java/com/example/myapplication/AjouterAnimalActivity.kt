package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AjouterAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_animal)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val speciesSpinner = findViewById<Spinner>(R.id.speciesSpinner)
        val sexSpinner = findViewById<Spinner>(R.id.sexSpinner)
        val birthDateInput = findViewById<EditText>(R.id.birthDateInput)
        val identificationInput = findViewById<EditText>(R.id.identificationInput)
        val parent1Spinner = findViewById<Spinner>(R.id.parent1Spinner)
        val parent2Spinner = findViewById<Spinner>(R.id.parent2Spinner)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val backButton = findViewById<Button>(R.id.backButton)

        backButton.setOnClickListener {
            finish() // Ferme cette activité et revient à la précédente
        }

        // Masquer la ProgressBar au départ
        progressBar.visibility = ProgressBar.GONE

        // Charger la base de données
        val db = DatabaseProvider.getDatabase(this)
        val speciesDao = db.typeEspeceDao()
        val animalDao = db.animalDao()

        // Charger les espèces dans le Spinner
        CoroutineScope(Dispatchers.IO).launch {
            val speciesList = speciesDao.getAllTypeEspeces().map { it.nom }
            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(
                    this@AjouterAnimalActivity,
                    android.R.layout.simple_spinner_item,
                    speciesList + "Ajouter une espèce" // Ajouter une option pour ouvrir une popup
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                speciesSpinner.adapter = adapter
            }
        }

        // Configurer le Spinner pour le sexe
        val sexAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("M", "F"))
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter

        // Lorsque l'utilisateur sélectionne une espèce, charger les parents potentiels
        speciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSpecies = speciesSpinner.selectedItem.toString()
                if (selectedSpecies == "Ajouter une espèce") {
                    showAddSpeciesDialog(speciesDao, speciesSpinner)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val speciesId = speciesDao.getAllTypeEspeces().find { it.nom == selectedSpecies }?.id
                        if (speciesId != null) {
                            val mothers = animalDao.getAnimalsBySpeciesAndSex(speciesId, "F") // Femelles
                            val fathers = animalDao.getAnimalsBySpeciesAndSex(speciesId, "M") // Mâles
                            withContext(Dispatchers.Main) {
                                updateParentSpinners(parent1Spinner, mothers, "Inconnue")
                                updateParentSpinners(parent2Spinner, fathers, "Inconnu")
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Gérer le clic sur le bouton "Sauvegarder"
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val species = speciesSpinner.selectedItem?.toString()?.trim()
            val sex = sexSpinner.selectedItem?.toString()?.trim()
            val birthDate = birthDateInput.text.toString().trim()
            val identification = identificationInput.text.toString().trim()
            val parent1Id = (parent1Spinner.selectedItem as? Animal)?.id
            val parent2Id = (parent2Spinner.selectedItem as? Animal)?.id

            if (name.isNotEmpty() && species != null && sex != null && birthDate.isNotEmpty() && identification.isNotEmpty()) {
                progressBar.visibility = ProgressBar.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val speciesId = speciesDao.getAllTypeEspeces().find { it.nom == species }?.id
                        if (speciesId != null) {
                            animalDao.insertAnimal(
                                Animal(
                                    nom = name,
                                    especeid = speciesId,
                                    sexe = sex,
                                    datedenaissance = birthDate,
                                    vivant = true,
                                    identificationparent1 = parent1Id,
                                    identificationparent2 = parent2Id,
                                    identification = identification
                                )
                            )
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@AjouterAnimalActivity,
                                    "Animal ajouté avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = ProgressBar.GONE
                                resetFields(nameInput, birthDateInput, identificationInput)
                                finish()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@AjouterAnimalActivity,
                                    "Erreur : espèce introuvable",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = ProgressBar.GONE
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@AjouterAnimalActivity,
                                "Erreur lors de l'ajout de l'animal : ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar.visibility = ProgressBar.GONE
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs correctement", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateParentSpinners(spinner: Spinner, animals: List<Animal>, unknownOption: String) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf(unknownOption) + animals.map { it.nom } // Ajouter l'option "Inconnu(e)" en premier
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun showAddSpeciesDialog(speciesDao: TypeEspeceDao, speciesSpinner: Spinner) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_species, null)
        val speciesNameInput = dialogView.findViewById<EditText>(R.id.speciesNameInput)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Ajouter une espèce")
            .setView(dialogView)
            .setPositiveButton("Ajouter") { _, _ ->
                val speciesName = speciesNameInput.text.toString().trim()
                if (speciesName.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        speciesDao.insert(TypeEspece(nom = speciesName))
                        val speciesList = speciesDao.getAllTypeEspeces().map { it.nom }
                        withContext(Dispatchers.Main) {
                            val adapter = ArrayAdapter(
                                this@AjouterAnimalActivity,
                                android.R.layout.simple_spinner_item,
                                speciesList + "Ajouter une espèce"
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            speciesSpinner.adapter = adapter
                            speciesSpinner.setSelection(adapter.getPosition(speciesName))
                        }
                    }
                }
            }
            .setNegativeButton("Annuler", null)
            .create()
        dialog.show()
    }

    private fun resetFields(vararg fields: EditText) {
        fields.forEach { it.text.clear() }
    }
}
