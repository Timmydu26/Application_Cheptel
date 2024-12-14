package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "animal",
    foreignKeys = [
        ForeignKey(
            entity = TypeEspece::class, // Référence à l'entité Espece
            parentColumns = ["id"], // Colonne de la table Espece
            childColumns = ["especeid"], // Colonne de cette table (Animal) qui référence Espece
            onDelete = ForeignKey.CASCADE // Suppression en cascade
        ),
        ForeignKey(
            entity = Animal::class, // Référence à un autre Animal pour parent 1
            parentColumns = ["id"],
            childColumns = ["identificationparent1"],
            onDelete = ForeignKey.SET_NULL // Si le parent 1 est supprimé, laisser NULL
        ),
        ForeignKey(
            entity = Animal::class, // Référence à un autre Animal pour parent 2
            parentColumns = ["id"],
            childColumns = ["identificationparent2"],
            onDelete = ForeignKey.SET_NULL // Si le parent 2 est supprimé, laisser NULL
        )
    ]
)
data class Animal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID unique pour chaque animal
    val nom: String, // Nom de l'animal
    val especeid: Int, // Référence à une espèce (ID de la table Espece)
    val sexe: String, // Sexe de l'animal (M/F)
    val datedenaissance: String, // Date de naissance
    val vivant: Boolean, // Indique si l'animal est vivant ou non
    val identificationparent1: Int?, // Référence optionnelle à un autre animal comme parent 1
    val identificationparent2: Int?, // Référence optionnelle à un autre animal comme parent 2
    val identification: String // Identifiant unique pour l'animal
)