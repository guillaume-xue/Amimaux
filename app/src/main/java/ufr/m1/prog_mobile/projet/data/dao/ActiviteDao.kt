package ufr.m1.prog_mobile.projet.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ufr.m1.prog_mobile.projet.data.entity.Activite

@Dao
interface ActiviteDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivite(activite: Activite): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivites(activite: List<Activite>)

    @Query("SELECT * FROM Activite")
    fun getActivites(): Flow<List<Activite>>

    @Query("SELECT * FROM Activite WHERE id = :id")
    fun getActiviteById(id: Int): Activite

    @Query("DELETE FROM Activite")
    suspend fun clearAllActivites()

    @Query("DELETE FROM Activite WHERE id = :id")
    suspend fun deleteActivite(id: Int)
}