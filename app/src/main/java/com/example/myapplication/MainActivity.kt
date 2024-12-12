package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récuper le bouton démarrer
        val startButton = findViewById<Button>(R.id.btnStart)

        //val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        /* Ouvrir la base de donnée */
        val db = DatabaseProvider.getDatabase(this)
        val animalDao = db.animalDao()

        // lancement d'une coroutine
        CoroutineScope(Dispatchers.IO).launch {
            // ex ajouter un animal
            //animalDao.insertAnimal(Animal(name = "Lion", age = 5))
            // recuperer la base de donnée dans animals
            val animals = animalDao.getAllAnimals()
            Log.d("MainActivity", "Animaux dans la base de données : $animals")

            /*runOnUiThread {
                recyclerView.adapter = AnimalAdapter(animals)
            }*/
        }



        startButton.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivity(intent)
        }
    }
}