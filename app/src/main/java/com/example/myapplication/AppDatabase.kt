package com.example.myapplication

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Database(entities = [Animal::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    // Associer le DAO à la base de données
    abstract fun animalDao(): AnimalDao
}