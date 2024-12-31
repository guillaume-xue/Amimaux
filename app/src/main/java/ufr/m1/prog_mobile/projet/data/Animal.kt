package ufr.m1.prog_mobile.projet.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["nom", "espece", "photo"], unique = true)])
data class Animal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String,
    val espece: String,
    val photo: String? = null
)