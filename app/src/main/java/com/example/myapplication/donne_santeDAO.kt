package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DonneeSanteDao {
    @Insert
    suspend fun insertDonneeSante(donneeSante: DonneeSante)

    @Query("SELECT * FROM donnee_sante WHERE animal_id = :animalId")
    suspend fun getDonneesSanteForAnimal(animalId: Int): List<DonneeSante>
}