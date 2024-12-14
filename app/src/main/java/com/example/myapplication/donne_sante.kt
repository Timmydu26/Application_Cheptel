package com.example.myapplication

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "donnee_sante",
        foreignKeys = [
            ForeignKey(entity = Animal::class, parentColumns = ["id"], childColumns = ["animal_id"]),
            ForeignKey(entity = TypeActe::class, parentColumns = ["id"], childColumns = ["type_evenement_id"])
        ]
)
data class DonneeSante(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val animal_id: Int,
    val type_evenement_id: Int,
    val date_realisation: String,
    val date_rappel: String?,
    val rappel_realise: Boolean
)

