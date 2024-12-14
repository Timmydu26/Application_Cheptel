package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_espece")
data class TypeEspece(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String
)