package ufr.m1.prog_mobile.projet.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import ufr.m1.prog_mobile.projet.data.Animal
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as MyApplication
            val model = app.viewModel
            LaunchedEffect(Unit) {
                model.initializeData()
            }
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
            .background(Color(0xFFE0E0E0)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier.padding(100.dp)
        )
        ImageScrollView(modifier = Modifier.weight(1f), animaux)
        MyButton(modifier = Modifier.align(Alignment.End), onClearAnimal = model::clearDatabaseAnimal, model::clearDatabaseActivite, model::clearDatabaseActiviteAnimal)
    }
}

@Composable
fun MyButton(modifier: Modifier, onClearAnimal: () -> Unit = {}, onClearActivite: () -> Unit = {}, onClearActiviteAnimal: () -> Unit = {}) {
    val context = LocalContext.current
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0x48B0B0B0)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {  },
            modifier = modifier
                .padding(8.dp)
            ) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendrier")
        }
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
                onClearAnimal()
                onClearActivite()
                onClearActiviteAnimal()
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

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
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
        }
    }
}