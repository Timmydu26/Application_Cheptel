package com.example.myapplication

import androidx.room.Embedded
import androidx.room.Relation

data class AnimalWithRelations(
    @Embedded val animal: Animal, // L'entité Animal
    @Relation(
        parentColumn = "espece_id",
        entityColumn = "id"
    )
    val espece: Espece? // Relation avec l'entité Espece
)
