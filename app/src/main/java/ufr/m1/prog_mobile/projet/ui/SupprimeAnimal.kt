package ufr.m1.prog_mobile.projet.ui

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ufr.m1.prog_mobile.projet.data.NotifDelay
import ufr.m1.prog_mobile.projet.ui.theme.ProjetTheme

class SupprimeAnimal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val app = application as MyApplication
                    val model = app.viewModel
                    Greeting2(
                        modifier = Modifier.padding(innerPadding),
                        model = model
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(modifier: Modifier, model: MyViewModel) {
    val select = remember { mutableStateOf("") }
    Column (modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
        ) {
        BackButton()
        SelectAnimal(model = model, select = select)
        ButtonValidSuppr(modifier, model = model, select = select.value)
    }

}

@SuppressLint("StateFlowValueCalledInComposition", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAnimal(model: MyViewModel, select: MutableState<String>) {
    val animaux by model.animals.collectAsState(listOf())
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        OutlinedTextField(
            value = select.value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Animal") },
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
            for (animal in animaux) {
                DropdownMenuItem(
                    text = { Text(text = animal.nom) },
                    onClick = {
                        select.value = animal.nom
                        expanded = false
                    }
                )
            }

        }
    }
}

@Composable
fun ButtonValidSuppr(modifier: Modifier, model: MyViewModel, select: String){
    val context = LocalContext.current
    Button(
        onClick = {
            when(select){
                "" -> Toast.makeText(context, "Veuillez selectionner un animal", Toast.LENGTH_SHORT).show()
                else -> {
                    model.deleteAnimal(select)
                    model.deleteActiviteAnimalByAnimal(select)
                }

            }
            val iii = Intent(context, MainActivity::class.java)
            context.startActivity(iii)
        },
        modifier = modifier.padding(16.dp)
    ) {
        Text("Supprimer")
    }
}