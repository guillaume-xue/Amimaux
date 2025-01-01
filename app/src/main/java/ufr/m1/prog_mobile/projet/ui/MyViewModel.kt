package ufr.m1.prog_mobile.projet.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.data.Animal
import ufr.m1.prog_mobile.projet.data.AnimalBD

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val dao by lazy { AnimalBD.getDB(application).MyDao() }
    private val list = application.resources.getStringArray(R.array.animals)
    private var erreurIns= mutableStateOf(false)

    fun addAnimal(nom: String, espece: String, photo: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dao.insertAnimal(Animal(nom = nom, espece = espece, photo = photo))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    private val prefix = mutableStateOf("")
    var animals = dao.getAnimalsPref(prefix.value)

    fun remplirAnimaux() {
        for (i in list.indices step 3) {
            addAnimal(nom = list[i], espece = list[i + 1], photo = list[i + 2])
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            dao.clearAllAnimals()
        }
    }


}