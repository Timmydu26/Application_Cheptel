package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "typeespece")
data class TypeEspece(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String
)