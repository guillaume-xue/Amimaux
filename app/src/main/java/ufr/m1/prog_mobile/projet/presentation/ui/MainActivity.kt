package ufr.m1.prog_mobile.projet.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ufr.m1.prog_mobile.projet.presentation.ui.theme.ProjetTheme
import ufr.m1.prog_mobile.projet.presentation.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as MyApplication
            val model = app.viewModel
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { MyTopBar() }) { innerPadding ->
                    MyMenuScreen(modifier = Modifier.padding(innerPadding), model = model)
                }
            }
        }
    }
}

@Composable
fun MyMenuScreen(modifier: Modifier, model: MainViewModel) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageScrollView(model = model, modifier = Modifier.weight(1f))
        MenuButtonBar(modifier = Modifier.align(Alignment.End), model = model)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar() = TopAppBar(
    title = {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Amimaux", style = MaterialTheme.typography.displayMedium)
        }
    },
    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFFFFF)),
    modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
)

@Composable
fun MenuButtonBar(modifier: Modifier, model: MainViewModel) {
    val context = LocalContext.current
    Row (
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(Color(0x48B0B0B0)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {model.onInfoButtonClick(context)},
            modifier = modifier
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "Info")
        }
        IconButton(
            onClick = {model.onAddButtonClick(context)},
            modifier = modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFB0B0B0))
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter")
        }
        IconButton(
            onClick = {model.onDeleteButtonClick(context)},
            modifier = modifier
                .padding(8.dp)
            ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}

@Composable
fun ImageScrollView(modifier: Modifier = Modifier, model: MainViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        for (animal in model.getAnimals()) {
            Image(
                painter = painterResource(id = context.resources.getIdentifier(animal.photo, "drawable", context.packageName)),
                contentDescription = animal.nom,
                modifier = Modifier
                    .padding(16.dp)
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        model.addAnimalSelect(animal)
                    }
                    .border(2.dp, model.getColorSelect(animal), RoundedCornerShape(16.dp))
            )

        }
    }
}

//@Composable
//fun Notif(model : MyViewModel) {
//    val vm = WorkManager.getInstance(context = LocalContext.current)
//    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())
//    val activites by model.activites.collectAsState(listOf())
//    for (activiteAnimal in activitesAnimals) {
//        when (activiteAnimal.frequence) {
//            NotifDelay.Unique -> {
//                val data = workDataOf(
//                    "animal" to activiteAnimal.animal,
//                    "activite" to activites.find { it.id == activiteAnimal.id }?.texte,
//                )
//                val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
//                    .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer), TimeUnit.SECONDS)
//                    .setInputData(data)
//                    .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
//                    .build()
//                vm.enqueue(workRequest)
//            }
//            NotifDelay.Quotidien -> {
//                val data = workDataOf(
//                    "animal" to activiteAnimal.animal,
//                    "activite" to activites.find { it.id == activiteAnimal.id }?.texte
//                )
//                val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
//                    .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 1), TimeUnit.SECONDS)
//                    .setInputData(data)
//                    .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
//                    .build()
//                vm.enqueue(workRequest)
//            }
//            NotifDelay.Hebdomadaire -> {
//                val data = workDataOf(
//                    "animal" to activiteAnimal.animal,
//                    "activite" to activites.find { it.id == activiteAnimal.id }?.texte
//                )
//                val workRequest = PeriodicWorkRequestBuilder<MyWorker>(7, TimeUnit.DAYS)
//                    .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 7), TimeUnit.SECONDS)
//                    .setInputData(data)
//                    .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
//                    .build()
//                vm.enqueue(workRequest)
//            }
//        }
//    }
//}
//
//@Composable
//fun calculateDelayInMillis(timeString: String, days: Int? = null): Long {
//    val time = timeString.split(":")
//    val targetHour = time[0].toInt()
//    val targetMinute = time[1].toInt()
//
//// Heure actuelle
//    val now = LocalTime.now()
//    val nowInSeconds = now.hour * 3600 + now.minute * 60 + now.second
//
//// Heure cible
//    val targetInSeconds = targetHour * 3600 + targetMinute * 60
//
//// Calculer la durée entre maintenant et l'heure cible
//    val durationInSeconds = if (nowInSeconds < targetInSeconds) {
//        targetInSeconds - nowInSeconds
//    } else {
//        // Si l'heure cible est déjà passée aujourd'hui, ajouter 24 heures
//        targetInSeconds + 86400 - nowInSeconds
//    }
//// Retourner la durée en secondes
//    return durationInSeconds.toLong()
//}
//
//
//@Composable
//fun NotifAdd(model: MyViewModel, animal: String, activite: String){
//    val vm = WorkManager.getInstance(context = LocalContext.current)
//    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())
//    val activites by model.activites.collectAsState(listOf())
//    val activiteSearch = activites.find { it.texte == activite } ?: return
//    val activiteAnimal = activitesAnimals.find { it.animal == animal && it.id == activiteSearch.id } ?: return
//    when (activiteAnimal.frequence) {
//        NotifDelay.Unique -> {
//            val data = workDataOf(
//                "animal" to activiteAnimal.animal,
//                "activite" to activites.find { it.id == activiteAnimal.id }?.texte
//            )
//            val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
//                .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer), TimeUnit.SECONDS)
//                .setInputData(data)
//                .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
//                .build()
//            vm.enqueue(workRequest)
//        }
//        NotifDelay.Quotidien -> {
//            val data = workDataOf(
//                "animal" to activiteAnimal.animal,
//                "activite" to activites.find { it.id == activiteAnimal.id }?.texte
//            )
//            val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
//                .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 1), TimeUnit.SECONDS)
//                .setInputData(data)
//                .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
//                .build()
//            vm.enqueue(workRequest)
//        }
//        NotifDelay.Hebdomadaire -> {
//            val data = workDataOf(
//                "animal" to activiteAnimal.animal,
//                "activite" to activites.find { it.id == activiteAnimal.id }?.texte
//            )
//            val workRequest = PeriodicWorkRequestBuilder<MyWorker>(7, TimeUnit.DAYS)
//                .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 7), TimeUnit.SECONDS)
//                .setInputData(data)
//                .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
//                .build()
//            vm.enqueue(workRequest)
//        }
//    }
//}
//
//@Composable
//fun RemoveActiNotif(model: MyViewModel, animal: String, actID: Int){
//    val vm = WorkManager.getInstance(context = LocalContext.current)
//    vm.cancelAllWorkByTag(animal + "_" + actID)
//}
