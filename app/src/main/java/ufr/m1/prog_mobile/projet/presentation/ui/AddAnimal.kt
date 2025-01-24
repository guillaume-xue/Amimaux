package ufr.m1.prog_mobile.projet.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.presentation.ui.theme.ProjetTheme
import ufr.m1.prog_mobile.projet.presentation.viewmodel.AddAniViewModel

class AddAnimal : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = application as MyApplication
            val model = app.addAniViewModel
            model.initializeData()
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAddScreen(modifier = Modifier.padding(innerPadding), model = model)
                }
            }
        }
    }

}

@Composable
fun MyAddScreen(modifier: Modifier, model: AddAniViewModel) {
    Column (
        modifier = modifier
            .padding(16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BackToMenuButton()
        EnterText(model = model)
        Box (
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun EnterText(model: AddAniViewModel){
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        IconButton(
            onClick = { },
            modifier = Modifier
                .padding(50.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFB0B0B0))
        ) {
            Image(
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "Ajouter",
                modifier = Modifier.size(50.dp)
            )
        }
        TextField(
            value = model.nom.value,
            onValueChange = {model.nom.value = it},
            label = { Text("Nom") },
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
        )
        TextField(
            value = model.espece.value,
            onValueChange = {model.espece.value = it},
            label = { Text("Espèce") },
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
        )
        Button(
            onClick = {
                model.addAnimal(context)
            },
            modifier = Modifier
                .padding(64.dp)
                .clip(RoundedCornerShape(50))
        ) {
            Text("Ajouter")
        }
    }

}


