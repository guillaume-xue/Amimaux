package ufr.m1.prog_mobile.projet.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import ufr.m1.prog_mobile.projet.data.Activite
import ufr.m1.prog_mobile.projet.data.ActiviteAnimal
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
    val ajouter = remember { mutableStateOf(true) }
    val texte = remember { mutableStateOf("") }

    val activites by model.activites.collectAsState(listOf())
    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())
    val activiteList = remember { mutableStateListOf<String>() }

    val selectActiviteColor = remember { mutableStateListOf<Color>() }
    val selectActivite = remember { mutableStateListOf<String>() }

    for (activiteAnimal in activitesAnimals) {
        if (activiteAnimal.animal == animal.nom) {
            val activite = activites.find { it.id == activiteAnimal.id }
            activiteList.add(activite?.texte ?: "Inconnu")
        }
        selectActiviteColor.add(Color.Transparent)
    }

    Column (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BackButton()
        AnimalImage(animal)
        AnimalInfo(animal)
        ActiviteList(activiteList, selectActiviteColor, selectActivite)
        TextAjoutActivite(texte)
        RadioButtonValide(ajouter)
        ButtonValide(context, texte, ajouter, animal.nom, selectActivite,activitesAnimals, activites, model::addActiviteAnimal, model::addActivite, model::deleteActiviteAnimal, model::deleteActivite)
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

@Composable
fun ActiviteList(
    activiteList: SnapshotStateList<String>,
    selectActiviteColor: SnapshotStateList<Color>,
    selectActivite: SnapshotStateList<String>
) {

    LazyColumn (
        modifier = Modifier
            .padding(16.dp)
    ){
        itemsIndexed(activiteList) { index, activite ->
            Text(
                activite,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(selectActiviteColor[index])
                    .clickable {
                        selectActiviteColor[index] = if (selectActiviteColor[index] == Color.Transparent) Color(0xFFB0B0B0) else Color.Transparent
                        if (selectActiviteColor[index] == Color.Transparent) {
                            selectActivite.remove(activite)
                        } else {
                            selectActivite.add(activite)
                        }
                    }
            )
            if (index < activiteList.size - 1) {
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }

}

@Composable
fun TextAjoutActivite(texte: MutableState<String>){
    OutlinedTextField(
        value = texte.value,
        onValueChange = {texte.value = it},
        label = { Text("Ajouter une activité") },
        modifier = Modifier
            .padding(16.dp)
            .height(50.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ButtonValide(
    context: Activity,
    text: MutableState<String>,
    ajouter: MutableState<Boolean>,
    nom: String,
    selectActivite: SnapshotStateList<String>,
    activitesAnimals: List<ActiviteAnimal>,
    activites: List<Activite>,
    onAddActAni: (Int, String) -> Unit,
    onAddAct: (Int?, String) -> Unit,
    onDelActAni: (Int, String) -> Unit,
    onDelAct: (Int) -> Unit
) {
    Button(
        onClick = {
            when {
                text.value == "" && ajouter.value -> Toast.makeText(context, "Veuillez entrer une activité", Toast.LENGTH_SHORT).show()
                selectActivite.isEmpty() && !ajouter.value -> Toast.makeText(context, "Veuillez sélectionner une activité", Toast.LENGTH_SHORT).show()
                ajouter.value -> {
                    // Ajouter
                    onAddAct(null, text.value)
                    onAddActAni(activites.size+1, nom)
                }
                !ajouter.value -> {
                    // Supprimer
                    for (select in selectActivite) {
                        val activite = activites.find { it.texte == select }
                        val activiteAnimal = activitesAnimals.find { it.id == activite?.id && it.animal == nom }
                        if (activiteAnimal != null) {
                            onDelActAni(activiteAnimal.id, nom)
                            if (activite != null) {
                                if(activite.id!! > 4){
                                    onDelAct(activite.id)
                                }
                            }
                        }
                    }
                }
            }
            text.value = ""
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Valider")
    }
}

@Composable
fun RadioButtonValide(ajouter: MutableState<Boolean>){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ){
        RadioButton(
            selected = ajouter.value,
            onClick = {
                ajouter.value = true
            },
        )
        Text(text = "Valider")
        RadioButton(
            selected = !ajouter.value,
            onClick = {
                ajouter.value = false
            }
        )
        Text(text = "Supprimer")
    }


}