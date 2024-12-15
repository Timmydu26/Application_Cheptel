package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Animal::class, TypeEspece::class, TypeActe::class, DonneeSante::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun animalDao(): AnimalDao
    abstract fun typeEspeceDao(): TypeEspeceDao
    abstract fun donneeSanteDao(): DonneeSanteDao // Ajoutez cette ligne pour inclure DonneeSanteDao
    abstract fun tyoeacteDao(): TypeActeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                )
                    .addCallback(DatabaseCallback()) // Ajout du Callback
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Callback pour pré-remplir la base de données
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    val speciesDao = database.typeEspeceDao()
                    speciesDao.insertAll(
                        listOf(
                            TypeEspece(nom = "chien"),
                            TypeEspece(nom = "chat"),
                            TypeEspece(nom = "ovin"),
                            TypeEspece(nom = "bovin"),
                            TypeEspece(nom = "caprin")
                        )
                    )
                }
            }
        }
    }
}