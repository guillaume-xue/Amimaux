package ufr.m1.prog_mobile.projet.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ufr.m1.prog_mobile.projet.data.dao.ActiviteAnimalDao
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal

@Database(entities = [ActiviteAnimal::class], version = 2)
abstract class ActivityAnimalBD : RoomDatabase() {
    abstract fun ActiviteAnimalDao(): ActiviteAnimalDao

    companion object {
        @Volatile
        private var instance: ActivityAnimalBD? = null

        fun getDB(c: Context): ActivityAnimalBD {
            if (instance != null) return instance!!
            instance = Room.databaseBuilder(c.applicationContext, ActivityAnimalBD::class.java, "activites_animals")
                .fallbackToDestructiveMigration().build()
            return instance!!
        }
    }
}