package com.example.myapplication

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "my_database"
            )
                .fallbackToDestructiveMigration() // Réinitialise la base en cas de migration manquante
                .build()
            INSTANCE = instance
            instance
        }
    }
}


