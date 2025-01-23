package ufr.m1.prog_mobile.projet.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["nom", "espece", "photo"], unique = true)])
data class Animal(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val nom: String,
    val espece: String,
    val photo: String
)