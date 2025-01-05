package ufr.m1.prog_mobile.projet.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["id","animal", "frequence"],
    indices = [Index(value = ["id","animal"])]
)
data class ActiviteAnimal(
    val id: Int,
    val animal: String,
    val frequence: NotifDelay,
    val timer : String

)