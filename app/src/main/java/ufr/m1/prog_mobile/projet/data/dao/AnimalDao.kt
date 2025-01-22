package ufr.m1.prog_mobile.projet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ufr.m1.prog_mobile.projet.data.entity.Animal

@Dao
interface AnimalDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnimal(animal: Animal): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAnimals(animal: List<Animal>)

    @Query("SELECT * FROM Animal")
    fun getAnimals(): Flow<List<Animal>>

    @Query("SELECT * FROM Animal WHERE nom LIKE :pref || '%'")
    fun getAnimalsPref(pref: String): Flow<List<Animal>>

    @Query("DELETE FROM Animal")
    suspend fun clearAllAnimals()

    @Query("DELETE FROM Animal WHERE nom = :nom")
    suspend fun deleteAnimal(nom: String)
}