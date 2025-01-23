package ufr.m1.prog_mobile.projet.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ufr.m1.prog_mobile.projet.presentation.ui.theme.ProjetTheme
import ufr.m1.prog_mobile.projet.presentation.viewmodel.InfoViewModel

class AnimalInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialiser les données avant la composition
        val app = application as MyApplication
        val model = app.infoViewModel
        model.initializeData(this)
        
        setContent {
            ProjetTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()){ innerPadding ->
                    MonAnimalInfo(modifier = Modifier.padding(innerPadding), model = model)
                }
            }
        }
    }
}

@Composable
fun MonAnimalInfo(modifier: Modifier, model: InfoViewModel) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ){
        BackButton()
        AnimalImage(model)
        AnimalInfo(modifier, model)
        ActiviteList(modifier = Modifier.weight(1f), model = model)
    }
}

@Composable
fun AnimalImage(model: InfoViewModel) {
    val context = LocalContext.current
    Image(
        painter = painterResource(id = context.resources.getIdentifier(model.currentAnimal.value.photo, "drawable", context.packageName)),
        contentDescription = model.currentAnimal.value.nom,
        modifier = Modifier
            .padding(8.dp)
            .size(300.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun AnimalInfo(modifier: Modifier, model: InfoViewModel) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Text("Nom : " + model.currentAnimal.value.nom, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Espèce : " + model.currentAnimal.value.espece, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
    }
}

@Composable
fun ActiviteList(modifier: Modifier, model: InfoViewModel) {
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
            itemsIndexed(model.listActivity.value) { index, item ->
                Row (
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        item.texte,
                        modifier = modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        model.listActiviteAnimal.value[index].frequence.toString(),
                        modifier = modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        model.listActiviteAnimal.value[index].time,
                        modifier = modifier
                            .padding(2.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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