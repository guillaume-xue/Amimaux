package ufr.m1.prog_mobile.projet.presentation.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ufr.m1.prog_mobile.projet.data.database.ActiviteBD
import ufr.m1.prog_mobile.projet.data.database.ActivityAnimalBD
import ufr.m1.prog_mobile.projet.data.database.AnimalBD
import ufr.m1.prog_mobile.projet.data.entity.Activite
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.entity.Animal
import ufr.m1.prog_mobile.projet.presentation.ui.AddActivity

class InfoViewModel(application: Application) : AndroidViewModel(application) {

    private val animalDao by lazy { AnimalBD.getDB(application).AnimalDao() }
    val currentAnimal = mutableStateOf(Animal(0, "", "", "photo"))

    private val activiteAnimalDao by lazy { ActivityAnimalBD.getDB(application).ActiviteAnimalDao() }
    val listActiviteAnimal = mutableStateOf(listOf<ActiviteAnimal>())

    private val activityDao by lazy { ActiviteBD.getDB(application).ActiviteDao() }
    val listActivity = mutableStateOf(listOf<Activite>())

    fun initializeData(context: Context) {
        // Nettoyer les variables sur le thread principal
        viewModelScope.launch(Dispatchers.Main) {
            currentAnimal.value = Animal(0, "", "", "photo")
            listActiviteAnimal.value = listOf()
            listActivity.value = listOf()

            // Récupérer l'ID depuis l'intent
            val intent = (context as Activity).intent
            val id = intent.getStringExtra("id")!!.toInt()

            // Charger les nouvelles données
            withContext(Dispatchers.IO) {
                val animal = animalDao.getAnimalById(id)
                val activitesAnimal = activiteAnimalDao.getActiviteAnimalById(animalId = id)
                val activities = mutableListOf<Activite>()
                
                for (activiteAnimal in activitesAnimal) {
                    val activite = activityDao.getActiviteById(activiteAnimal.activityId)
                    activities.add(activite)
                }

                // Mettre à jour les états sur le thread principal
                withContext(Dispatchers.Main) {
                    currentAnimal.value = animal
                    listActiviteAnimal.value = activitesAnimal
                    listActivity.value = activities
                }
            }
        }
    }

    fun onButtonAddAct(context: Context) {
        val intent = Intent(context, AddActivity::class.java)
        intent.putExtra("id", currentAnimal.value.id.toString())
        context.startActivity(intent)
    }
}