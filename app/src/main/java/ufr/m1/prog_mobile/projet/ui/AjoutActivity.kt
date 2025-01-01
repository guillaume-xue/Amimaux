package ufr.m1.prog_mobile.projet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme

class AjoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MonAjout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MonAjout(modifier: Modifier){
    MonAjoutPreview()
}

@Preview(showBackground = true)
@Composable
fun MonAjoutPreview() {

    var nom by remember { mutableStateOf("") }
    var espece by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        IconButton(
            onClick = {},
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFB0B0B0))
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp),
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "Ajouter"
            )
        }
        TextField(
            value = nom,
            onValueChange = {nom = it},
            label = { Text("Nom") },
            modifier = Modifier
                .padding(8.dp)
                .height(50.dp)
        )
        TextField(
            value = espece,
            onValueChange = {espece = it},
            label = { Text("Espèce") },
            modifier = Modifier
                .padding(8.dp)
                .height(50.dp)
        )
        Button(
            onClick = {},
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(50))
        ) {
            Text("Ajouter")
        }

    }

}