package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimalListeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_liste)

        // Initaliser le bouton retour
        val backButton = findViewById<Button>(R.id.backButton)
        // Initialiser RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.animalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Récupérer la liste des animaux depuis la base de données
        val db = DatabaseProvider.getDatabase(this)
        val animalDao = db.animalDao()


        CoroutineScope(Dispatchers.IO).launch {
            val animals = animalDao.getAllAnimals() // Appel suspendu dans un thread IO
            withContext(Dispatchers.Main) {
                val adapter = AnimalAdapter(animals)
                recyclerView.adapter = adapter
                // Mettre à jour RecyclerView sur le thread principal
                //recyclerView.adapter = AnimalAdapter(animals)
            }
        }

        // Configurer le bouton "Retour"
        backButton.setOnClickListener {
            finish() // Terminer l'activité actuelle et revenir à l'activité précédente
        }
    }
}
