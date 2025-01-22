package ufr.m1.prog_mobile.projet.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id","texte"], unique = true)])
data class Activite(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val texte: String,
)