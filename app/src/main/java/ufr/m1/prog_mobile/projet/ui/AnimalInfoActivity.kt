package ufr.m1.prog_mobile.projet.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import ufr.m1.prog_mobile.projet.data.Activite
import ufr.m1.prog_mobile.projet.data.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.Animal
import ufr.m1.prog_mobile.projet.data.NotifDelay
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme

class AnimalInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as MyApplication
            val model = app.viewModel
            ProjetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()){ innerPadding ->
                    MonAnimalInfo(modifier = Modifier.padding(innerPadding), model)
                }
            }
        }
    }
}

@Composable
fun MonAnimalInfo(modifier: Modifier, model: MyViewModel) {
    val animaux by model.animals.collectAsState(listOf())
    val activites by model.activites.collectAsState(listOf())
    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())

    val context = LocalContext.current
    val iii = (context as Activity).intent
    val nom = iii.getStringExtra("animal")

    val animal = animaux.find { it.nom == nom } ?: Animal("Inconnu", "Inconnu", "Inconnu")

    val activiteList = remember { mutableStateListOf<String>() }
    val activiteDelay = remember { mutableStateListOf<NotifDelay>() }
    val activiteTime = remember { mutableStateListOf<String>() }

    val selectActiviteColor = remember { mutableStateListOf<Color>() }

    activiteList.clear()
    activiteDelay.clear()
    activiteTime.clear()
    selectActiviteColor.clear()

    for (activiteAnimal in activitesAnimals) {
        if (activiteAnimal.animal == nom) {
            val activite = activites.find { it.id == activiteAnimal.id }
            activiteDelay.add(activiteAnimal.frequence)
            activiteTime.add(activiteAnimal.timer)
            activiteList.add(activite?.texte ?: "Inconnu")
            selectActiviteColor.add(Color.Transparent)
        }
    }

    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ){
        BackButton()
        AnimalImage(animal)
        AnimalInfo(modifier, animal)
        ActiviteList(modifier = Modifier.weight(1f), activiteList, activiteDelay, activiteTime)
//        ActiviteList(activiteList, selectActiviteColor, selectActivite)
//        TextAjoutActivite(texte)
//        RadioButtonValide(ajouter)
//        ButtonValide(context, texte, ajouter, animal.nom, selectActivite,activitesAnimals, activites, model::addActiviteAnimal, model::addActivite, model::deleteActiviteAnimal, model::deleteActivite)

        if (nom != null) {
            ButtonAjoutActi(modifier = Modifier, context = context, nom = nom)
        }
    }
}

@Composable
fun AnimalInfo(modifier: Modifier, animal: Animal) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Text("Nom : " + animal.nom, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Espèce : " + animal.espece, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
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
                .padding(8.dp)
                .size(300.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        val uri = Uri.parse(photo)
        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = animal.nom,
            modifier = Modifier
                .padding(8.dp)
                .size(300.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    }
}

@Composable
fun ActiviteList(
    modifier: Modifier,
    activiteList: SnapshotStateList<String>,
    activiteDelay: SnapshotStateList<NotifDelay>,
    activiteTime: SnapshotStateList<String>
) {
    Column (
        modifier = modifier.verticalScroll(rememberScrollState())
    ){
        LazyColumn (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp)
        ){
            itemsIndexed(activiteList) { index, activite ->
                Row (
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        activite,
                        modifier = modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        activiteDelay[index].name,
                        modifier = modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        activiteTime[index],
                        modifier = modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider()
            }
    }

    }
}

@Composable
fun ButtonAjoutActi(
    modifier: Modifier,
    context: Activity,
    nom: String
) {

    Button(
        onClick = {
            val iii = Intent(context, AjoutActivite::class.java)
            iii.putExtra("animal", nom)
            context.startActivity(iii)
        },
        modifier = modifier
            .padding(8.dp)
    ) {
        Text("Ajouter une activité")
    }

}





