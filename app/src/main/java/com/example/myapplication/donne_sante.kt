package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donneesante")
data class DonneeSante(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,         // Clé primaire auto-générée
    val animalId: Int,                                       // Lien vers l'animal concerné
    val typeActeId: Int,                                     // Lien vers le type d'acte (ex: vaccination, stérilisation)
    val dateRealisation: String,                            // Date de réalisation de l'acte
    val dateRappel: String? = null,                         // Date optionnelle pour le rappel
    val rappelRealise: Boolean = false,                     // Statut du rappel (true = fait, false = non)
    val details: String                                     // Description ou détails de l'acte
)
