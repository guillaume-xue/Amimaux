package ufr.m1.prog_mobile.projet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ufr.m1.prog_mobile.projet.data.Animal
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme
import kotlin.reflect.KFunction0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MonMenu(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview
@Composable
fun MonMenu(modifier: Modifier = Modifier, model: MyViewModel = viewModel()) {

    val animaux by model.animals.collectAsState(listOf())

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        ListAnimal(animaux, model::remplirAnimaux)
        MyButton()
    }
}

@Composable
fun MyButton(){
    Row (
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {}
        ) { Text("Ajouter") }
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {}
        ) { Text("Supprimer") }

    }
}

@Composable
fun ListAnimal(animaux: List<Animal>, onRemplirAuthors: () -> Unit) {
    onRemplirAuthors()
    LazyColumn (
        modifier = Modifier.padding(16.dp)
    ) { items(animaux) {
        Row {
            Text(text = it.nom)
        }
    } }
}