package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_acte")
data class TypeActe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String
)