package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Animal
import com.example.myapplication.DatabaseProvider
import com.example.myapplication.AppDatabase

@Dao
interface AnimalDao {
    // Insérer un animal dans la table
    @Insert
    suspend fun insertAnimal(animal: Animal)

    // Récupérer tous les animaux
    @Query("SELECT * FROM animals")
    suspend fun getAllAnimals(): List<Animal>

    // Supprimer tous les animaux
    @Query("DELETE FROM animals")
    suspend fun deleteAllAnimals(): Int // Retourne le nombre de lignes supprimées
}
