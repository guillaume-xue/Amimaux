package ufr.m1.prog_mobile.projet.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id","animal"], unique = true)])
data class ActiviteAnimal(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val animal: String,
)