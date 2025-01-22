package ufr.m1.prog_mobile.projet.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ufr.m1.prog_mobile.projet.data.dao.AnimalDao
import ufr.m1.prog_mobile.projet.data.entity.Animal

@Database(entities = [Animal::class], version = 3)
abstract class AnimalBD : RoomDatabase() {
    abstract fun AnimalDao(): AnimalDao

    companion object {
        @Volatile
        private var instance: AnimalBD? = null

        fun getDB(c: Context): AnimalBD {
            if (instance != null) return instance!!
            instance = Room.databaseBuilder(c.applicationContext, AnimalBD::class.java, "animals")
                .fallbackToDestructiveMigration().build()
            return instance!!
        }
    }
}