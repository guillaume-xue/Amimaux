package ufr.m1.prog_mobile.projet.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ufr.m1.prog_mobile.projet.R
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme
import coil.compose.rememberImagePainter

class AjoutActivity : ComponentActivity() {
    var selectedImageUri: Uri? = null

    private val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            // Handle the selected image URI (e.g., display it in an Image composable)
        } else {
            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
        }
    }

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

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getImage.launch(intent)
    }
}

@Composable
fun MonAjout(modifier: Modifier){
    MonAjoutPreview()
}

@Composable
fun MonAjoutPreview() {

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BackButtun()
        EntreText()
        Box (
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun EntreText(onAddAnimal : (nom: String, espece: String, photo: String?) -> Unit = { nom, espece, photo -> }){
    val context = LocalContext.current
    var nom by remember { mutableStateOf("") }
    var espece by remember { mutableStateOf("") }
    val activity = context as AjoutActivity
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        IconButton(
            onClick = { activity.openGallery() },
            modifier = Modifier
                .padding(50.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFB0B0B0))
        ) {
            if (activity.selectedImageUri != null) {
                Image(
                    painter = rememberImagePainter(activity.selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier.size(50.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.photo),
                    contentDescription = "Ajouter",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        TextField(
            value = nom,
            onValueChange = {nom = it},
            label = { Text("Nom") },
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
        )
        TextField(
            value = espece,
            onValueChange = {espece = it},
            label = { Text("Espèce") },
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
        )
        Button(
            onClick = {
                if (nom.isEmpty() || espece.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Les champs ne peuvent pas être vides",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onAddAnimal(nom, espece, activity.selectedImageUri?.toString())
                }
            },
            modifier = Modifier
                .padding(64.dp)
                .clip(RoundedCornerShape(50))
        ) {
            Text("Ajouter")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun BackButtun(){
    val context = LocalContext.current
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ){
        IconButton(
            onClick = {
                val iii = Intent(context, MainActivity::class.java)
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


