package ufr.m1.prog_mobile.projet.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.data.Activite
import ufr.m1.prog_mobile.projet.data.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.ActiviteAnimalBD
import ufr.m1.prog_mobile.projet.data.ActiviteBD
import ufr.m1.prog_mobile.projet.data.Animal
import ufr.m1.prog_mobile.projet.data.AnimalBD

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private var erreurIns= mutableStateOf(false)
    private val prefix = mutableStateOf("")

    // AnimalDao
    private val AnimalDao by lazy { AnimalBD.getDB(application).AnimalDao() }
    private val AnimalList = application.resources.getStringArray(R.array.animals)


    fun addAnimal(nom: String, espece: String, photo: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = AnimalDao.insertAnimal(Animal(nom = nom, espece = espece, photo = photo))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    var animals = AnimalDao.getAnimalsPref(prefix.value)

    fun remplirAnimaux() {
        for (i in AnimalList.indices step 3) {
            addAnimal(nom = AnimalList[i], espece = AnimalList[i + 1], photo = AnimalList[i + 2])
        }
    }

    fun clearDatabaseAnimal() {
        viewModelScope.launch {
            AnimalDao.clearAllAnimals()
        }
    }

    // ActiviteDao

    private val ActiviteDao by lazy { ActiviteBD.getDB(application).ActiviteDao() }
    private val ActiviteList = application.resources.getStringArray(R.array.activites)


    fun addActivite(id: Int?, texte: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ActiviteDao.insertActivite(Activite(id = id, texte = texte))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    var activites = ActiviteDao.getActivitesPref(prefix.value)

    fun remplirActivites() {
        for (i in ActiviteList.indices step 2) {
            addActivite(id = ActiviteList[i].toInt(), texte = ActiviteList[i+1])
        }
    }

    fun clearDatabaseActivite() {
        viewModelScope.launch {
            ActiviteDao.clearAllActivites()
        }
    }

    fun deleteActivite(id: Int) {
        viewModelScope.launch {
            ActiviteDao.deleteActivite(id)
        }
    }

    // ActiviteAnimalDao

    private val ActiviteAnimalDao by lazy { ActiviteAnimalBD.getDB(application).ActiviteAnimalDao() }
    private val ActiviteAnimalList = application.resources.getStringArray(R.array.activites_animals)

    fun addActiviteAnimal(id: Int, animal: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ActiviteAnimalDao.insertActiviteAnimal(ActiviteAnimal(id = id, animal = animal))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    var activiteAnimals = ActiviteAnimalDao.getActiviteAnimalsPref(prefix.value)

    fun remplirActivitesAnimaux() {
        for (i in ActiviteAnimalList.indices step 2) {
            addActiviteAnimal(id = ActiviteAnimalList[i].toInt(), animal = ActiviteAnimalList[i+1])
        }
    }

    fun clearDatabaseActiviteAnimal() {
        viewModelScope.launch {
            ActiviteAnimalDao.clearAllActiviteAnimals()
        }
    }

    fun deleteActiviteAnimal(id: Int, animal: String) {
        viewModelScope.launch {
            ActiviteAnimalDao.deleteActiviteAnimal(id, animal)
        }
    }

}