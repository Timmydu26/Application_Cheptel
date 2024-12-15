package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
            finish()
        }

        progressBar.visibility = ProgressBar.GONE

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
                    speciesList + "Ajouter une espèce"
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                speciesSpinner.adapter = adapter
            }
        }

        // Configurer le Spinner pour le sexe
        val sexAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("M", "F"))
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter

        // Charger les parents potentiels après la sélection de l'espèce
        speciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSpecies = speciesSpinner.selectedItem.toString()
                if (selectedSpecies == "Ajouter une espèce") {
                    showAddSpeciesDialog(speciesDao, speciesSpinner)
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val speciesId = speciesDao.getAllTypeEspeces().find { it.nom == selectedSpecies }?.id
                        if (speciesId != null) {
                            val mothers = animalDao.getAnimalsBySpeciesAndSex(speciesId, "F")
                            val fathers = animalDao.getAnimalsBySpeciesAndSex(speciesId, "M")
                            withContext(Dispatchers.Main) {
                                updateParentSpinner(parent1Spinner, mothers, "Inconnue")
                                updateParentSpinner(parent2Spinner, fathers, "Inconnu")
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Sauvegarder un nouvel animal
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val species = speciesSpinner.selectedItem?.toString()?.trim()
            val sex = sexSpinner.selectedItem?.toString()?.trim()
            val birthDate = birthDateInput.text.toString().trim()
            val identification = identificationInput.text.toString().trim()
            val parent1Id = (parent1Spinner.selectedItem as? Animal)?.id
            val parent2Id = (parent2Spinner.selectedItem as? Animal)?.id

            Log.d("DEBUG_PARENTS", "Parent1 ID: $parent1Id, Parent2 ID: $parent2Id")

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
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@AjouterAnimalActivity,
                                "Erreur : ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar.visibility = ProgressBar.GONE
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateParentSpinner(spinner: Spinner, animals: List<Animal>, unknownOption: String) {
        val adapter = object : ArrayAdapter<Animal>(
            this,
            android.R.layout.simple_spinner_item,
            listOf(Animal(0, unknownOption, 0, "", "", true, null, null, "")) + animals
        ) {
            override fun getItem(position: Int): Animal? = super.getItem(position)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).text = getItem(position)?.nom
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).text = getItem(position)?.nom
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun showAddSpeciesDialog(speciesDao: TypeEspeceDao, speciesSpinner: Spinner) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_species, null)
        val speciesNameInput = dialogView.findViewById<EditText>(R.id.speciesNameInput)

        AlertDialog.Builder(this)
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
            .show()
    }

    private fun resetFields(vararg fields: EditText) {
        fields.forEach { it.text.clear() }
    }
}
