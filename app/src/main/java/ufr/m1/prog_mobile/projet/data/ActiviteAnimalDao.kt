package ufr.m1.prog_mobile.projet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiviteAnimalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActiviteAnimal(activiteAnimal: ActiviteAnimal): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActiviteAnimals(activiteAnimal: List<ActiviteAnimal>)

    @Query("SELECT * FROM ActiviteAnimal")
    fun getActiviteAnimals(): Flow<List<ActiviteAnimal>>

    @Query("SELECT * FROM ActiviteAnimal WHERE id LIKE :pref || '%'")
    fun getActiviteAnimalsPref(pref: String): Flow<List<ActiviteAnimal>>

    @Query("DELETE FROM ActiviteAnimal")
    suspend fun clearAllActiviteAnimals()

    @Query("DELETE FROM ActiviteAnimal WHERE id = :id AND animal = :animal")
    suspend fun deleteActiviteAnimal(id: Int, animal: String)

    @Query("DELETE FROM ActiviteAnimal WHERE animal = :animal")
    suspend fun deleteActiviteAnimalByAnimal(animal: String)
}