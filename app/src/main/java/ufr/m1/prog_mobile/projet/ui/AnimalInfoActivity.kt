package ufr.m1.prog_mobile.projet.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import ufr.m1.prog_mobile.projet.data.Animal
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme

class AnimalInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MonAnimalInfo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MonAnimalInfo(modifier: Modifier, model: MyViewModel = viewModel()) {
    val animaux by model.animals.collectAsState(listOf())
    val context = LocalContext.current
    val iii = (context as Activity).intent
    val nom = iii.getStringExtra("animal")
    val animal = animaux.find { it.nom == nom } ?: Animal("Inconnu", "Inconnu", "Inconnu")
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BackButtun()
        AnimalImage(animal)
        AnimalInfo(animal)
    }
}

@Composable
fun AnimalInfo(animal: Animal) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Nom : " + animal.nom)
        Text("Espèce : " + animal.espece)
    }
}

@Composable
fun AnimalImage(animal: Animal) {
    val context = LocalContext.current
    val photo = animal.photo
    if (photo == "chat" || photo == "chien" || photo == "hamster" || photo == "poisson") {
        Image(
            painter = painterResource(id = context.resources.getIdentifier(photo, "drawable", context.packageName)),
            contentDescription = animal.nom,
            modifier = Modifier
                .padding(16.dp)
                .size(300.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        val uri = Uri.parse(photo)
        Image(
            painter = rememberImagePainter(data = uri),
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