package ufr.m1.prog_mobile.projet.data.entity

import androidx.room.Entity
import androidx.room.Index
import ufr.m1.prog_mobile.projet.data.NotifDelay

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