package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TypeEspeceDao {

    // Insère une seule espèce et retourne l'identifiant généré
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(typeEspece: TypeEspece): Long

    // Insère une liste d'espèces et retourne les identifiants générés
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(typeEspeces: List<TypeEspece>): List<Long>

    // Récupère toutes les espèces
    @Query("SELECT * FROM typeespece")
    suspend fun getAllTypeEspeces(): List<TypeEspece>
}
