package ufr.m1.prog_mobile.projet.presentation.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ufr.m1.prog_mobile.projet.data.database.ActiviteBD
import ufr.m1.prog_mobile.projet.data.database.ActivityAnimalBD
import ufr.m1.prog_mobile.projet.data.database.AnimalBD
import ufr.m1.prog_mobile.projet.data.entity.Activite
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.entity.Animal

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    private val animalDao by lazy { AnimalBD.getDB(application).AnimalDao() }
    val currentAnimal = mutableStateOf(Animal(0, "", "", "photo"))

    private val activiteAnimalDao by lazy { ActivityAnimalBD.getDB(application).ActiviteAnimalDao() }
    val listActiviteAnimal = mutableStateOf(listOf<ActiviteAnimal>())

    private val activityDao by lazy { ActiviteBD.getDB(application).ActiviteDao() }
    val listActivity = mutableStateOf(listOf<Activite>())

    // Fonctions pour la base de données

    fun initializeData(context: Context) {

        val intent = (context as Activity).intent
        val id = intent.getStringExtra("id")!!.toInt()
        val list = listActivity.value.toMutableList()
        viewModelScope.launch(Dispatchers.IO) {
            currentAnimal.value = animalDao.getAnimalById(id)
            listActiviteAnimal.value = activiteAnimalDao.getActiviteAnimalById(animalId = id)
        }
        for (activiteAnimal in listActiviteAnimal.value) {
            viewModelScope.launch(Dispatchers.IO) {
                list.add(activityDao.getActiviteById(activiteAnimal.activityId))
            }
        }
        listActivity.value = list
    }

    init {
        currentAnimal.value = Animal(0, "", "", "photo")
        listActiviteAnimal.value = listOf()
        listActivity.value = listOf()
    }
}