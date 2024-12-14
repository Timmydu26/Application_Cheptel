package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TypeEspeceDao {
    @Insert
    suspend fun insertTypeEspece(typeEspece: TypeEspece)

    @Query("SELECT * FROM type_espece")
    suspend fun getAllTypeEspeces(): List<TypeEspece>
}