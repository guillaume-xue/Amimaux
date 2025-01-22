package ufr.m1.prog_mobile.projet.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.rememberAsyncImagePainter
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.data.entity.Animal
import ufr.m1.prog_mobile.projet.data.NotifDelay
import ufr.m1.prog_mobile.projet.presentation.notification.MyWorker
import ufr.m1.prog_mobile.projet.presentation.ui.theme.ProjetTheme
import ufr.m1.prog_mobile.projet.presentation.viewmodel.MyViewModel
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var boo = false;


        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ boo = it }
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)

        enableEdgeToEdge()
        setContent {
            val app = application as MyApplication
            val model = app.viewModel
            LaunchedEffect(Unit) {
                model.initializeData()
            }
            Notif(model)
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { MonTopBar() }) { innerPadding ->
                    MonMenu(modifier = Modifier.padding(innerPadding), model = model)
                }
            }
        }

    }
}

@Composable
fun MonMenu(modifier: Modifier, model: MyViewModel) {
    val animaux by model.animals.collectAsState(listOf())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageScrollView(modifier = Modifier.weight(1f), animaux)
        MyButton(modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun MyButton(modifier: Modifier) {
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
            onClick = {
                val iii = Intent(context, AjoutAnimal::class.java)
                context.startActivity(iii)
            },
            modifier = modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFB0B0B0))
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter")
        }
        IconButton(
            onClick = {
                val iii = Intent(context, SupprimeAnimal::class.java)
                context.startActivity(iii)
                      },
            modifier = modifier
                .padding(8.dp)
            ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer")
        }
    }
}

@Composable
fun ImageScrollView(modifier: Modifier, animaux: List<Animal>) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        for (animal in animaux) {
            animal.photo?.let { photo ->
                if (photo == "chat" || photo == "chien" || photo == "hamster" || photo == "poisson") {
                    Image(
                        painter = painterResource(id = context.resources.getIdentifier(photo, "drawable", context.packageName)),
                        contentDescription = animal.nom,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val iii = Intent(context, AnimalInfoActivity::class.java)
                                iii.putExtra("animal", animal.nom)
                                context.startActivity(iii)
                            }
                    )
                } else if(photo == ""){
                    Image(
                        painter = painterResource(id = R.drawable.photo),
                        contentDescription = "Ajouter",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                val iii = Intent(context, AjoutAnimal::class.java)
                                context.startActivity(iii)
                            }
                    )
                } else {
                    val uri = Uri.parse(photo)
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = animal.nom,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {

                            }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonTopBar() = TopAppBar(
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
fun Notif(model : MyViewModel) {
    val vm = WorkManager.getInstance(context = LocalContext.current)
    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())
    val activites by model.activites.collectAsState(listOf())
    for (activiteAnimal in activitesAnimals) {
        when (activiteAnimal.frequence) {
            NotifDelay.Unique -> {
                val data = workDataOf(
                    "animal" to activiteAnimal.animal,
                    "activite" to activites.find { it.id == activiteAnimal.id }?.texte,
                )
                val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
                    .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer), TimeUnit.SECONDS)
                    .setInputData(data)
                    .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
                    .build()
                vm.enqueue(workRequest)
            }
            NotifDelay.Quotidien -> {
                val data = workDataOf(
                    "animal" to activiteAnimal.animal,
                    "activite" to activites.find { it.id == activiteAnimal.id }?.texte
                )
                val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
                    .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 1), TimeUnit.SECONDS)
                    .setInputData(data)
                    .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
                    .build()
                vm.enqueue(workRequest)
            }
            NotifDelay.Hebdomadaire -> {
                val data = workDataOf(
                    "animal" to activiteAnimal.animal,
                    "activite" to activites.find { it.id == activiteAnimal.id }?.texte
                )
                val workRequest = PeriodicWorkRequestBuilder<MyWorker>(7, TimeUnit.DAYS)
                    .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 7), TimeUnit.SECONDS)
                    .setInputData(data)
                    .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
                    .build()
                vm.enqueue(workRequest)
            }
        }
    }
}

@Composable
fun calculateDelayInMillis(timeString: String, days: Int? = null): Long {
    val time = timeString.split(":")
    val targetHour = time[0].toInt()
    val targetMinute = time[1].toInt()

// Heure actuelle
    val now = LocalTime.now()
    val nowInSeconds = now.hour * 3600 + now.minute * 60 + now.second

// Heure cible
    val targetInSeconds = targetHour * 3600 + targetMinute * 60

// Calculer la durée entre maintenant et l'heure cible
    val durationInSeconds = if (nowInSeconds < targetInSeconds) {
        targetInSeconds - nowInSeconds
    } else {
        // Si l'heure cible est déjà passée aujourd'hui, ajouter 24 heures
        targetInSeconds + 86400 - nowInSeconds
    }
// Retourner la durée en secondes
    return durationInSeconds.toLong()
}


@Composable
fun NotifAdd(model: MyViewModel, animal: String, activite: String){
    val vm = WorkManager.getInstance(context = LocalContext.current)
    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())
    val activites by model.activites.collectAsState(listOf())
    val activiteSearch = activites.find { it.texte == activite } ?: return
    val activiteAnimal = activitesAnimals.find { it.animal == animal && it.id == activiteSearch.id } ?: return
    when (activiteAnimal.frequence) {
        NotifDelay.Unique -> {
            val data = workDataOf(
                "animal" to activiteAnimal.animal,
                "activite" to activites.find { it.id == activiteAnimal.id }?.texte
            )
            val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer), TimeUnit.SECONDS)
                .setInputData(data)
                .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
                .build()
            vm.enqueue(workRequest)
        }
        NotifDelay.Quotidien -> {
            val data = workDataOf(
                "animal" to activiteAnimal.animal,
                "activite" to activites.find { it.id == activiteAnimal.id }?.texte
            )
            val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 1), TimeUnit.SECONDS)
                .setInputData(data)
                .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
                .build()
            vm.enqueue(workRequest)
        }
        NotifDelay.Hebdomadaire -> {
            val data = workDataOf(
                "animal" to activiteAnimal.animal,
                "activite" to activites.find { it.id == activiteAnimal.id }?.texte
            )
            val workRequest = PeriodicWorkRequestBuilder<MyWorker>(7, TimeUnit.DAYS)
                .setInitialDelay(calculateDelayInMillis(activiteAnimal.timer, 7), TimeUnit.SECONDS)
                .setInputData(data)
                .addTag(activiteAnimal.animal + "_" + activiteAnimal.id)
                .build()
            vm.enqueue(workRequest)
        }
    }
}

@Composable
fun RemoveActiNotif(model: MyViewModel, animal: String, actID: Int){
    val vm = WorkManager.getInstance(context = LocalContext.current)
    vm.cancelAllWorkByTag(animal + "_" + actID)
}
