package com.example.myapli


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapli.ui.theme.MyapliTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyapliTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    // Recuperar color guardado en SharedPreferences
    val savedColor = sharedPreferences.getString("bg_color", "White") ?: "White"
    val backgroundColor = getColorFromString(savedColor)

    var newNoteTitle by remember { mutableStateOf("") }
    var newNoteContent by remember { mutableStateOf("") }

    // Firebase Firestore
    val firestore = FirebaseFirestore.getInstance()

    // Guardar nota en Firebase
    fun addNoteToFirestore(title: String, content: String) {
        val note = hashMapOf(
            "title" to title,
            "content" to content
        )
        firestore.collection("notes")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(context, "Nota agregada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al agregar nota", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Gestor de Notas",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_notes),
            contentDescription = "Ícono de Notas",
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo de título de la nota con hint
        OutlinedTextField(
            value = newNoteTitle,
            onValueChange = { newNoteTitle = it },
            label = { Text("Título de la nota") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            maxLines = 1,
            isError = newNoteTitle.isEmpty() && newNoteContent.isEmpty()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Campo de contenido de la nota con hint
        OutlinedTextField(
            value = newNoteContent,
            onValueChange = { newNoteContent = it },
            label = { Text("Contenido de la nota") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp),
            maxLines = 10,
            isError = newNoteContent.isEmpty() && newNoteTitle.isEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Cerrar el teclado cuando se presiona 'Done'
                }
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (newNoteTitle.isNotEmpty() && newNoteContent.isNotEmpty()) {
                    addNoteToFirestore(newNoteTitle, newNoteContent)
                    newNoteTitle = ""
                    newNoteContent = ""
                } else {
                    Toast.makeText(context, "Por favor, ingrese título y contenido", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Nota")
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, DetailActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Notas")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuraciones")
            }
        }
    }
}

// Función para convertir nombre de color en Color de Jetpack Compose
fun getColorFromString(colorName: String): Color {
    return when (colorName) {
        "Blue" -> Color.Blue
        "Green" -> Color.Green
        "Yellow" -> Color.Yellow
        else -> Color.White
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyapliTheme {
        MainScreen()
    }
}
