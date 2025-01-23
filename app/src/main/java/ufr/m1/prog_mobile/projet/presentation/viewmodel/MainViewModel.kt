package ufr.m1.prog_mobile.projet.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.data.NotifDelay
import ufr.m1.prog_mobile.projet.data.database.ActiviteBD
import ufr.m1.prog_mobile.projet.data.database.ActivityAnimalBD
import ufr.m1.prog_mobile.projet.data.database.AnimalBD
import ufr.m1.prog_mobile.projet.data.entity.Activite
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.entity.Animal
import ufr.m1.prog_mobile.projet.presentation.ui.AddAnimal
import ufr.m1.prog_mobile.projet.presentation.ui.AnimalInfoActivity

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var erreurIns= mutableStateOf(false)

    private val animalDao by lazy { AnimalBD.getDB(application).AnimalDao() }
    private val animalList = application.resources.getStringArray(R.array.noms_animaux)
    private val especeList = application.resources.getStringArray(R.array.especes_animaux)
    private val photoList = application.resources.getStringArray(R.array.photos_animaux)

    private val activityAnimalDao by lazy { ActivityAnimalBD.getDB(application).ActiviteAnimalDao() }
    private val activityIDList = application.resources.getStringArray(R.array.activityID)
    private val animalIDList = application.resources.getStringArray(R.array.animalID)
    private val frequenceList = application.resources.getStringArray(R.array.frequences)
    private val timeList = application.resources.getStringArray(R.array.times)

    private val activityDao by lazy { ActiviteBD.getDB(application).ActiviteDao() }
    private val activiteIDList = application.resources.getStringArray(R.array.activiteID)
    private val activityList = application.resources.getStringArray(R.array.activites)

    private val animals by lazy { animalDao.getAnimals() }
    private val listAnimalSelect = mutableStateOf(listOf<Animal>())

    // Fonctions pour la base de données
    private fun addAnimal(nom: String, espece: String, photo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = animalDao.insertAnimal(Animal(nom = nom, espece = espece, photo = photo))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
    }

    private fun addAnimals() {
        for (i in animalList.indices) {
            addAnimal(nom = animalList[i], espece = especeList[i], photo = photoList[i])
        }
    }

    private fun addActivityAnimal(activityId: Int, animalId: Int, frequence: NotifDelay, time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            activityAnimalDao.insertActiviteAnimal(ActiviteAnimal(activityId, animalId, frequence, time))
        }
    }

    private fun addActivitysAnimals(){
        for (i in activityIDList.indices) {
            addActivityAnimal(activityIDList[i].toInt(), animalIDList[i].toInt(), NotifDelay.valueOf(frequenceList[i]), timeList[i])
        }
    }

    private fun addActivity(id: Int, texte: String) {
        viewModelScope.launch(Dispatchers.IO) {
            activityDao.insertActivite(Activite(id = id, texte = texte))
        }
    }

    private fun addActivities(){
        for (i in activityList.indices) {
            addActivity(activiteIDList[i].toInt(), activityList[i])
        }
    }

    private fun deleteAnimal(nom: String) {
        viewModelScope.launch(Dispatchers.IO) {
            animalDao.deleteAnimal(nom)
        }
    }

    fun initializeData(){
        addAnimals()
        addActivitysAnimals()
        addActivities()
    }

    @Composable
    fun getAnimals() = animals.collectAsState(listOf()).value

    // Fonctions pour les boutons
    fun onAddButtonClick(context: Context) {
        val intent = Intent(context, AddAnimal::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun onDeleteButtonClick(context: Context) {
        if (listAnimalSelect.value.isEmpty()) {
            Toast.makeText(context, "Veuillez selectionner un animal", Toast.LENGTH_SHORT).show()
            return
        }
        for (animal in listAnimalSelect.value) {
            deleteAnimal(animal.nom)
        }
        listAnimalSelect.value = listOf()
    }

    fun onInfoButtonClick(context: Context){
        if (listAnimalSelect.value.isEmpty()) {
            Toast.makeText(context, "Veuillez selectionner un animal", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(context, AnimalInfoActivity::class.java)
        intent.putExtra("id", listAnimalSelect.value[listAnimalSelect.value.lastIndex].id.toString())
        context.startActivity(intent)
        listAnimalSelect.value = listOf()
    }

    fun addAnimalSelect(animal: Animal) {
        val list = listAnimalSelect.value.toMutableList()
        if (list.contains(animal)) {
            list.remove(animal)
        } else {
            list.add(animal)
        }
        listAnimalSelect.value = list
    }

    fun getColorSelect(animal: Animal): Color {
        return if (listAnimalSelect.value.contains(animal)) {
            Color.Black
        } else {
            Color.Transparent
        }
    }


}