package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DonneeSanteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonneeSante(donneeSante: DonneeSante)

    @Query("SELECT * FROM donneesante WHERE animalId = :animalId")
    suspend fun getDonneesSanteForAnimal(animalId: Int): List<DonneeSante>

    @Query("UPDATE donneesante SET rappelRealise = :rappelRealise WHERE id = :id")
    suspend fun updateRappelStatus(id: Int, rappelRealise: Boolean)
}
