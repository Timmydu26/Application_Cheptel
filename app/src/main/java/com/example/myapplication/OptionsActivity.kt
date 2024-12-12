package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.content.Intent


class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_options)

        val btnAfficherCheptel = findViewById<Button>(R.id.btnAfficherCheptel)
        val btnModifierAnimal = findViewById<Button>(R.id.btnModifierAnimal)
        val btnAjouterAnimal = findViewById<Button>(R.id.btnAjouterAnimal)
        val btnSupprimerAnimal = findViewById<Button>(R.id.btnSupprimerAnimal)

        btnAfficherCheptel.setOnClickListener {
            Log.d("ActivityOptions", "Afficher cheptel")
            val intent = Intent(this, AnimalListeActivity::class.java)
            startActivity(intent)
            // Logique pour afficher le cheptel
        }

        btnModifierAnimal.setOnClickListener {
            Log.d("ActivityOptions", "Modifier animal")
            // Logique pour modifier un animal
        }

        btnAjouterAnimal.setOnClickListener {
            Log.d("ActivityOptions", "Ajouter animal")
            val intent = Intent(this, AjouterAnimalActivity::class.java)
            startActivity(intent)
            // Logique pour ajouter un animal
        }

        btnSupprimerAnimal.setOnClickListener {
            Log.d("ActivityOptions", "Suprimer animal")
            // Logique pour supprimer un animal
        }
    }
}
