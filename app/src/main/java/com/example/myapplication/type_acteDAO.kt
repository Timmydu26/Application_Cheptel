package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TypeActeDao {
    @Insert
    suspend fun insertTypeActe(typeActe: TypeActe)

    @Query("SELECT * FROM type_acte")
    suspend fun getAllTypeActes(): List<TypeActe>
}