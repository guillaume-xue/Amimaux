package ufr.m1.prog_mobile.projet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ActiviteAnimal::class], version = 2)
abstract class ActiviteAnimalBD : RoomDatabase() {
    abstract fun ActiviteAnimalDao(): ActiviteAnimalDao

    companion object {
        @Volatile
        private var instance: ActiviteAnimalBD? = null

        fun getDB(c: Context): ActiviteAnimalBD {
            if (instance != null) return instance!!
            instance = Room.databaseBuilder(c.applicationContext, ActiviteAnimalBD::class.java, "activites_animals")
                .fallbackToDestructiveMigration().build()
            return instance!!
        }
    }
}