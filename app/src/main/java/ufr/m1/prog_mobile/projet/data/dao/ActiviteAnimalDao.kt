package ufr.m1.prog_mobile.projet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal

@Dao
interface ActiviteAnimalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActiviteAnimal(activiteAnimal: ActiviteAnimal): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActiviteAnimals(activiteAnimal: List<ActiviteAnimal>)

    @Query("SELECT * FROM ActiviteAnimal")
    fun getActiviteAnimals(): Flow<List<ActiviteAnimal>>

    @Query("SELECT * FROM ActiviteAnimal WHERE animalId = :animalId")
    fun getActiviteAnimalById(animalId: Int): List<ActiviteAnimal>

    @Query("DELETE FROM ActiviteAnimal")
    suspend fun clearAllActiviteAnimals()

}