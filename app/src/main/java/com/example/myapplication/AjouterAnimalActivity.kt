package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AjouterAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_animal)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val speciesInput = findViewById<EditText>(R.id.speciesInput)
        val sexInput = findViewById<EditText>(R.id.sexInput)
        val birthDateInput = findViewById<EditText>(R.id.birthDateInput)
        val lastAppointmentInput = findViewById<EditText>(R.id.lastAppointmentInput)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val db = DatabaseProvider.getDatabase(this)
        val animalDao = db.animalDao()

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val species = speciesInput.text.toString()
            val sex = sexInput.text.toString()
            val birthDate = birthDateInput.text.toString()
            val lastAppointment = lastAppointmentInput.text.toString()

            if (name.isNotEmpty() && species.isNotEmpty() && sex.isNotEmpty() && birthDate.isNotEmpty() && lastAppointment.isNotEmpty()) {
                // Ajouter l'animal à la base de données
                CoroutineScope(Dispatchers.IO).launch {
                    animalDao.insertAnimal(
                        Animal(
                            name = name,
                            species = species,
                            sex = sex,
                            birthDate = birthDate,
                            lastAppointment = lastAppointment
                        )
                    )
                    runOnUiThread {
                        Toast.makeText(this@AjouterAnimalActivity, "Animal ajouté avec succès", Toast.LENGTH_SHORT).show()
                        finish() // Fermer l'activité après l'ajout
                    }
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
