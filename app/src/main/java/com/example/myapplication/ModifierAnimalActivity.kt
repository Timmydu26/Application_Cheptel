package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModifierAnimalActivity : AppCompatActivity() {

    private lateinit var spinnerAnimal: Spinner
    private lateinit var spinnerTypeActe: Spinner

    private lateinit var vivantRadioButton: RadioButton
    private lateinit var mortRadioButton: RadioButton
    private lateinit var saveStateButton: Button

    private lateinit var dateActeEditText: EditText
    private lateinit var dateRappelEditText: EditText
    private lateinit var rappelRealiseCheckBox: CheckBox
    private lateinit var detailsEditText: EditText
    private lateinit var saveHealthButton: Button
    private lateinit var retourButton: Button // Bouton Retour

    private var animalList = listOf<Animal>()
    private var typeActeList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifier_animal)

        // Initialisation des vues
        spinnerAnimal = findViewById(R.id.spinnerSelectAnimal)
        spinnerTypeActe = findViewById(R.id.spinnerSelectTypeActe)

        vivantRadioButton = findViewById(R.id.radioVivant)
        mortRadioButton = findViewById(R.id.radioMort)
        saveStateButton = findViewById(R.id.buttonSaveState)

        dateActeEditText = findViewById(R.id.editTextDateActe)
        dateRappelEditText = findViewById(R.id.editTextDateRappel)
        rappelRealiseCheckBox = findViewById(R.id.checkBoxRappelRealise)
        detailsEditText = findViewById(R.id.editTextDetails)
        saveHealthButton = findViewById(R.id.buttonSaveHealth)
        retourButton = findViewById(R.id.buttonRetour)

        val db = AppDatabase.getDatabase(this)
        val animalDao = db.animalDao()
        val donneeSanteDao = db.donneeSanteDao()

        // Charger les animaux dans le spinner
        lifecycleScope.launch(Dispatchers.IO) {
            animalList = animalDao.getAllAnimals()
            val animalNames = animalList.map { it.nom }
            withContext(Dispatchers.Main) {
                spinnerAnimal.adapter = ArrayAdapter(
                    this@ModifierAnimalActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    animalNames
                )
            }
        }

        // Charger les types d'acte
        typeActeList = mutableListOf("Vaccination", "Stérilisation", "Autres", "Ajouter un type d'acte")
        spinnerTypeActe.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            typeActeList
        )

        // Enregistrer l'état civil
        saveStateButton.setOnClickListener {
            val selectedAnimal = animalList[spinnerAnimal.selectedItemPosition]
            val isAlive = vivantRadioButton.isChecked
            lifecycleScope.launch(Dispatchers.IO) {
                animalDao.updateEtatCivil(selectedAnimal.id, isAlive)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ModifierAnimalActivity, "État mis à jour", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Enregistrer une donnée de santé
        saveHealthButton.setOnClickListener {
            val selectedAnimal = animalList[spinnerAnimal.selectedItemPosition]
            val dateActe = dateActeEditText.text.toString()
            val dateRappel = dateRappelEditText.text.toString().ifEmpty { null }
            val rappelRealise = rappelRealiseCheckBox.isChecked
            val details = detailsEditText.text.toString()

            if (dateActe.isEmpty() || details.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les champs requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newDonneeSante = DonneeSante(
                animalId = selectedAnimal.id,
                typeActeId = spinnerTypeActe.selectedItemPosition + 1,
                dateRealisation = dateActe,
                dateRappel = dateRappel,
                rappelRealise = rappelRealise,
                details = details
            )

            lifecycleScope.launch(Dispatchers.IO) {
                donneeSanteDao.insertDonneeSante(newDonneeSante)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ModifierAnimalActivity, "Donnée ajoutée", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Gestion du bouton retour
        retourButton.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
