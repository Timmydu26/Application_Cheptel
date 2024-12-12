package com.example.myapplication

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    // Migration de la version 1 vers la version 2
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE animals ADD COLUMN species TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE animals ADD COLUMN sex TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE animals ADD COLUMN birthDate TEXT NOT NULL DEFAULT ''")
            database.execSQL("ALTER TABLE animals ADD COLUMN lastAppointment TEXT NOT NULL DEFAULT ''")
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "animal_database"
            )
                .addMigrations(MIGRATION_1_2) // Ajout de la migration ici
                .build()
            INSTANCE = instance
            instance
        }
    }
}



/*object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "cheptel_database"
            ).build()
        }
        return instance!!
    }
}*/