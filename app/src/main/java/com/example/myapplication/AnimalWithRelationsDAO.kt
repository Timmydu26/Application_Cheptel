package com.example.myapplication

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AnimalDao {
    @Transaction
    @Query("SELECT * FROM animals")
    suspend fun getAllAnimalsWithRelations(): List<AnimalWithRelations>
}