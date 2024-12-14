package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query

@Entity(
    tableName = "animals",
    foreignKeys = [
        ForeignKey(entity = Animal::class, parentColumns = ["id"], childColumns = ["identification_parent1"]),
        ForeignKey(entity = Animal::class, parentColumns = ["id"], childColumns = ["identification_parent2"]),
        ForeignKey(entity = TypeEspece::class, parentColumns = ["id"], childColumns = ["espece_id"])
    ]
)
data class Animal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String, //nom animal
    val espece_id: Int, // identifiant espece
    val sexe: String, // M ou F
    val date_de_naissance: String,
    val vivant: Boolean,
    val identification_parent1: Int?,
    val identification_parent2: Int?,
    val identification: String
)
