package ufr.m1.prog_mobile.projet.data.entity

import androidx.room.Entity
import androidx.room.Index
import ufr.m1.prog_mobile.projet.data.NotifDelay

@Entity(
    primaryKeys = ["activityId","animalId"],
    indices = [Index(value = ["activityId","animalId","frequence","time"])]
)
data class ActiviteAnimal(
    val activityId: Int,
    val animalId: Int,
    val frequence: NotifDelay,
    val time : String
)