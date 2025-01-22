package ufr.m1.prog_mobile.projet.presentation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ufr.m1.prog_mobile.projet.data.entity.Activite
import ufr.m1.prog_mobile.projet.data.entity.ActiviteAnimal
import ufr.m1.prog_mobile.projet.data.NotifDelay
import ufr.m1.prog_mobile.projet.presentation.ui.theme.ProjetTheme
import ufr.m1.prog_mobile.projet.presentation.viewmodel.MyViewModel

class AjoutActivite : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as MyApplication
            val model = app.viewModel
            ProjetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()){
                        innerPadding ->
                    Greeting(modifier = Modifier.padding(innerPadding), model)
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun Greeting(modifier: Modifier, model: MyViewModel) {

    val activites by model.activites.collectAsState(listOf())
    val activitesAnimals by model.activiteAnimals.collectAsState(listOf())

    val activiteList = remember { mutableStateListOf<String>() }
    val activiteDelay = remember { mutableStateListOf<NotifDelay>() }
    val activiteTime = remember { mutableStateListOf<String>() }

    val context = LocalContext.current
    val iii = (context as Activity).intent
    val nom = iii.getStringExtra("animal") ?: "Inconnu"

    val ajouter = remember { mutableStateOf(true) }
    val texte = remember { mutableStateOf("") }

    val selectActiviteColor = remember { mutableStateListOf<Color>() }
    val selectActivite = remember { mutableStateListOf<String>() }

    activiteList.clear()
    activiteDelay.clear()
    activiteTime.clear()
    selectActiviteColor.clear()
    for (activiteAnimal in activitesAnimals) {
        if (activiteAnimal.animal == nom) {
            val activite = activites.find { it.id == activiteAnimal.id }
//            Toast.makeText(context, "ceci : ${activite?.texte}", Toast.LENGTH_SHORT).show()
            activiteDelay.add(activiteAnimal.frequence)
            activiteTime.add(activiteAnimal.timer)
            activiteList.add(activite?.texte ?: "Inconnu")
            selectActiviteColor.add(Color.Transparent)
        }
    }


    Column (modifier = modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally){
        BackButton2(nom)
        SelectionAnimal(nom, model)

        ActiviteList(activiteList, selectActiviteColor, selectActivite, activiteDelay, activiteTime)
        TextAjoutActivite(texte)
        DelaySelection(model)
        TimerButton(model)
        RadioButtonValide(ajouter)
        ButtonValide(model, context, texte, ajouter, nom, selectActivite,activitesAnimals, activites, model::addActiviteAnimal, model::addActivite, model::deleteActiviteAnimal, model::deleteActivite)
    }

}

@SuppressLint("StateFlowValueCalledInComposition", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DelaySelection(model: MyViewModel){
    val delays = NotifDelay.entries
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = model.selectedDelayType.value.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Delay") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            delays.forEach { delay ->
                DropdownMenuItem(
                    text = { Text(text = delay.name) },
                    onClick = {
                        model.selectDelayType(delay)
                        expanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TimerButton(model: MyViewModel){
    val context = LocalContext.current
    val selectedTime by model.selectedTime.collectAsState()

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                // Format de l'heure : HH:MM
                model.selectTime(String.format("%02d:%02d", hourOfDay, minute))
            },
            12, // Heure par défaut
            0,  // Minute par défaut
            true // Format 24h
        )
    }
    Button(
        onClick = { timePickerDialog.show() },
        modifier = Modifier.fillMaxWidth()
    ){
        Text("Select Time : $selectedTime")
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SelectionAnimal(nom: String, model: MyViewModel) {

    OutlinedTextField(
        value = nom,
        onValueChange = {},
        readOnly = true,
        label = { Text("Animal") },
        modifier = Modifier
            .fillMaxWidth()
    )

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

@Composable
fun ButtonValide(
    model: MyViewModel,
    context: Activity,
    text: MutableState<String>,
    ajouter: MutableState<Boolean>,
    nom: String,
    selectActivite: SnapshotStateList<String>,
    activitesAnimals: List<ActiviteAnimal>,
    activites: List<Activite>,
    onAddActAni: (Int, String, NotifDelay, String) -> Unit,
    onAddAct: (Int?, String) -> Unit,
    onDelActAni: (Int, String) -> Unit,
    onDelAct: (Int) -> Unit,
) {
    var tmp by remember { mutableStateOf(false) }
    var tmp2 by remember { mutableStateOf(false) };
    var tmp4 by remember { mutableIntStateOf(-1) };

    val selectedTime by model.selectedTime.collectAsState()
    val selectedDelay by model.selectedDelayType.collectAsState()

    Button(
        onClick = {
            when {
                text.value == "" && ajouter.value -> Toast.makeText(context, "Veuillez entrer une activité", Toast.LENGTH_SHORT).show()
                selectActivite.isEmpty() && !ajouter.value -> Toast.makeText(context, "Veuillez sélectionner une activité", Toast.LENGTH_SHORT).show()
                ajouter.value -> {
                    // Ajouter
                    onAddAct(null, text.value)
                    onAddActAni(activites.size+1, nom, selectedDelay, selectedTime)
                    tmp = true
                }
                !ajouter.value -> {
                    // Supprimer
                    for (select in selectActivite) {
                        val activite = activites.find { it.texte == select }
                        val activiteAnimal = activitesAnimals.find { it.id == activite?.id && it.animal == nom }
                        if (activiteAnimal != null) {
                            onDelActAni(activiteAnimal.id, nom)
                            tmp2 = true
                            tmp4 = activiteAnimal.id
//                            if (activite != null) {
//                                if(activite.id!! > 4){
//                                    onDelAct(activite.id)
//                                }
//
//                            }
                        }
                    }
                    selectActivite.clear()
                }
            }
            text.value = ""
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text("Valider")
    }
    if(tmp){
//        Toast.makeText(context, "Activité ajoutée", Toast.LENGTH_SHORT).show()
        NotifAdd(model, nom, text.value)
        tmp = false
    }
    if(tmp2){
        RemoveActiNotif(model, nom, tmp4)
        tmp2 = false
    }
}

@Composable
fun TextAjoutActivite(texte: MutableState<String>){
    OutlinedTextField(
        value = texte.value,
        onValueChange = {texte.value = it},
        label = { Text(texte.value) },
        modifier = Modifier
            .padding(8.dp)
            .height(60.dp)
            .fillMaxWidth()

    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ActiviteList(
    activiteList: SnapshotStateList<String>,
    selectActiviteColor: SnapshotStateList<Color>,
    selectActivite: SnapshotStateList<String>,
    activiteDelay: SnapshotStateList<NotifDelay>,
    activiteTime: SnapshotStateList<String>
) {
    LazyColumn (
        modifier = Modifier
            .heightIn(max = 300.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        itemsIndexed(activiteList) { index, activite ->
            val text = activite + " : " + activiteDelay[index].name + " " + activiteTime[index]
                Text(
                    text,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(selectActiviteColor[index])
                        .clickable {
                            selectActiviteColor[index] = if (selectActiviteColor[index] == Color.Transparent) Color(
                                0xFFB6B6B6
                            ) else Color.Transparent
                            if (selectActiviteColor[index] == Color.Transparent) {
                                selectActivite.remove(activite)
                            } else {
                                selectActivite.add(activite)
                            }
                        }
                )

            if (index < activiteList.size - 1) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun BackButton2(nom: String){
    val context = LocalContext.current
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        IconButton(
            onClick = {
                val iii = Intent(context, AnimalInfoActivity::class.java)
                iii.putExtra("animal", nom)
                context.startActivity(iii)
            },
        ) {
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color(0xFF000000)
            )
        }
        Box (
            modifier = Modifier.padding(8.dp)
        )
    }
}
