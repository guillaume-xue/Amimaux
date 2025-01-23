package ufr.m1.prog_mobile.projet.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ufr.m1.prog_mobile.projet.data.database.AnimalBD
import ufr.m1.prog_mobile.projet.data.entity.Animal
import ufr.m1.prog_mobile.projet.presentation.ui.MainActivity

class AddAniViewModel (application: Application) : AndroidViewModel(application) {

    private var erreurIns= mutableStateOf(false)

    private val animalDao by lazy { AnimalBD.getDB(application).AnimalDao() }
    var nom = mutableStateOf("")
    var espece = mutableStateOf("")
    var photo = mutableStateOf("photo")

    // Fonctions pour la base de données
    fun addAnimal(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = animalDao.insertAnimal(Animal(nom = nom.value, espece = espece.value, photo = photo.value))
            if (result == -1L) {
                withContext(Dispatchers.Main) {
                    erreurIns.value = true
                }
            }
        }
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun initializeData(){
        nom.value = ""
        espece.value = ""
        photo.value = "photo"
    }

}