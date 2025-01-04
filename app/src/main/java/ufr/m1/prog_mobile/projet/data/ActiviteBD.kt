package ufr.m1.prog_mobile.projet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Activite::class], version = 3)
abstract class ActiviteBD : RoomDatabase() {
    abstract fun MyDao(): MyDao

    companion object {
        @Volatile
        private var instance: ActiviteBD? = null

        fun getDB(c: Context): ActiviteBD {
            if (instance != null) return instance!!
            instance = Room.databaseBuilder(c.applicationContext, ActiviteBD::class.java, "activites")
                .fallbackToDestructiveMigration().build()
            return instance!!
        }
    }
}