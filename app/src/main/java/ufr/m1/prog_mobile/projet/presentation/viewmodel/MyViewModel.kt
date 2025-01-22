package ufr.m1.prog_mobile.projet.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.data.entity.Activite
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.database.ActiviteAnimalBD
import ufr.m1.prog_mobile.projet.data.database.ActiviteBD
import ufr.m1.prog_mobile.projet.data.entity.Animal
import ufr.m1.prog_mobile.projet.data.database.AnimalBD
import ufr.m1.prog_mobile.projet.data.NotifDelay

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private var erreurIns= mutableStateOf(false)
    private val prefix = mutableStateOf("")
    private var isInitialized = false

    // AnimalDao
    private val animalDao by lazy { AnimalBD.getDB(application).AnimalDao() }
    private val animalList = application.resources.getStringArray(R.array.animals)


    fun addAnimal(nom: String, espece: String, photo: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = animalDao.insertAnimal(Animal(nom = nom, espece = espece, photo = photo))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    var animals = animalDao.getAnimalsPref(prefix.value)

    fun remplirAnimaux() {
        for (i in animalList.indices step 3) {
            addAnimal(nom = animalList[i], espece = animalList[i + 1], photo = animalList[i + 2])
        }
    }

    fun clearDatabaseAnimal() {
        viewModelScope.launch {
            animalDao.clearAllAnimals()
        }
    }

    fun deleteAnimal(nom: String) {
        viewModelScope.launch {
            animalDao.deleteAnimal(nom)
        }
    }

    // ActiviteDao

    private val activiteDao by lazy { ActiviteBD.getDB(application).ActiviteDao() }
    private val activiteList = application.resources.getStringArray(R.array.activites)


    fun addActivite(id: Int?, texte: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = activiteDao.insertActivite(Activite(id = id, texte = texte))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    var activites = activiteDao.getActivitesPref(prefix.value)

    fun remplirActivites() {
        for (i in activiteList.indices step 2) {
            addActivite(id = activiteList[i].toInt(), texte = activiteList[i+1])
        }
    }

    fun clearDatabaseActivite() {
        viewModelScope.launch {
            activiteDao.clearAllActivites()
        }
    }

    fun deleteActivite(id: Int) {
        viewModelScope.launch {
            activiteDao.deleteActivite(id)
        }
    }

    // ActiviteAnimalDao

    private val activiteAnimalDao by lazy { ActiviteAnimalBD.getDB(application).ActiviteAnimalDao() }
    private val activiteAnimalList = application.resources.getStringArray(R.array.activites_animals)

    fun addActiviteAnimal(id: Int, animal: String, frequence: NotifDelay, timer: String = "00:00") {
        viewModelScope.launch(Dispatchers.IO) {
            val result = activiteAnimalDao.insertActiviteAnimal(ActiviteAnimal(id = id, animal = animal, frequence =  frequence, timer = timer))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    var activiteAnimals = activiteAnimalDao.getActiviteAnimalsPref(prefix.value)

    fun remplirActivitesAnimaux() {
        for (i in activiteAnimalList.indices step 4) {
            var tmp : NotifDelay = NotifDelay.Unique    ;
            when (activiteAnimalList[i+2]) {
                "quotidien" -> tmp = NotifDelay.Quotidien
                "hebdomadaire" -> tmp = NotifDelay.Hebdomadaire
            }
            addActiviteAnimal(id = activiteAnimalList[i].toInt(), animal = activiteAnimalList[i+1], frequence = tmp, timer = activiteAnimalList[i+3])
        }
    }

    fun clearDatabaseActiviteAnimal() {
        viewModelScope.launch {
            activiteAnimalDao.clearAllActiviteAnimals()
        }
    }

    fun deleteActiviteAnimal(id: Int, animal: String) {
        viewModelScope.launch {
            activiteAnimalDao.deleteActiviteAnimal(id, animal)
        }
    }

    fun deleteActiviteAnimalByAnimal(animal: String) {
        viewModelScope.launch {
            activiteAnimalDao.deleteActiviteAnimalByAnimal(animal)
        }
    }

    fun initializeData() {
        if (!isInitialized) {
            remplirAnimaux()
            remplirActivites()
            remplirActivitesAnimaux()
            isInitialized = true
        }
    }

    private val _selectedAnimal = MutableStateFlow<Animal?>(null)
    val selectedAnimal: StateFlow<Animal?> = _selectedAnimal

    fun selectAnimal(animal: Animal) {
        viewModelScope.launch {
            _selectedAnimal.value = animal
        }
    }

    private val _selectedDelayType = MutableStateFlow<NotifDelay>(NotifDelay.Unique)
    val selectedDelayType: StateFlow<NotifDelay> = _selectedDelayType

    fun selectDelayType(delayType: NotifDelay) {
        viewModelScope.launch {
            _selectedDelayType.value = delayType
        }
    }

    private val _selectedTime = MutableStateFlow("00:00")
    val selectedTime: StateFlow<String> = _selectedTime

    fun selectTime(time: String) {
        viewModelScope.launch {
            _selectedTime.value = time
        }
    }



}