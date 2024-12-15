package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimal(animal: Animal)

    @Update
    suspend fun updateAnimal(animal: Animal)

    @Query("SELECT * FROM animal WHERE id = :id")
    suspend fun getAnimalById(id: Int): Animal?

    @Query("SELECT * FROM animal")
    suspend fun getAllAnimals(): List<Animal>

    @Query("DELETE FROM animal WHERE id = :id")
    suspend fun deleteAnimalById(id: Int)

    @Query("SELECT * FROM animal WHERE especeid = :speciesId AND sexe = :sex")
    suspend fun getAnimalsBySpeciesAndSex(speciesId: Int, sex: String): List<Animal>

    @Query("UPDATE animal SET vivant = :isVivant WHERE id = :animalId")
    suspend fun updateEtatCivil(animalId: Int, isVivant: Boolean)

}
