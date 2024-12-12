package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Cette classe représente la table "animals"
@Entity(tableName = "animals")
data class Animal(
    // Colonne "id" - Clé primaire, elle est auto-générée
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val species: String, // Espèce
    val sex: String, // Sexe
    val birthDate: String, // Date de naissance (au format String pour simplifier)
    val lastAppointment: String // Date du dernier rendez-vous (format String pour simplifier)
)