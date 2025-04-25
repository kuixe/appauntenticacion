package com.example.myapli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapli.ui.theme.MyapliTheme
import androidx.compose.runtime.livedata.observeAsState

class DetailActivity : ComponentActivity() {
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyapliTheme {
                DetailScreen(viewModel)
            }
        }
    }
}

class DetailViewModel : ViewModel() {
    private val _notas = MutableLiveData<List<String>>(
        listOf("Nota 1: Reunión a las 3PM", "Nota 2: Comprar leche", "Nota 3: Entregar proyecto")
    )
    val notas: LiveData<List<String>> = _notas

    fun removeNota(nota: String) {
        _notas.value = _notas.value?.filter { it != nota }
    }

    fun editarNota(notaVieja: String, notaNueva: String) {
        _notas.value = _notas.value?.map { if (it == notaVieja) notaNueva else it }
    }

    fun agregarNota(nuevaNota: String) {
        if (nuevaNota.isNotBlank()) {
            _notas.value = _notas.value?.plus(nuevaNota)
        }
    }
}

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val notas by viewModel.notas.observeAsState(emptyList())

    var notaEditando by remember { mutableStateOf<String?>(null) }
    var textoEditado by remember { mutableStateOf("") }
    var nuevaNota by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Mis Notas", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para agregar notas
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = nuevaNota,
                onValueChange = { nuevaNota = it },
                label = { Text("Nueva Nota") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    viewModel.agregarNota(nuevaNota)
                    nuevaNota = ""
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Nota")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(notas) { nota ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = nota, modifier = Modifier.weight(1f))

                        Row {
                            IconButton(onClick = { notaEditando = nota; textoEditado = nota }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Nota")
                            }

                            IconButton(onClick = { viewModel.removeNota(nota) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Nota")
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para editar nota
    if (notaEditando != null) {
        AlertDialog(
            onDismissRequest = { notaEditando = null },
            title = { Text("Editar Nota") },
            text = {
                OutlinedTextField(
                    value = textoEditado,
                    onValueChange = { textoEditado = it },
                    label = { Text("Nueva Nota") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.editarNota(notaEditando!!, textoEditado)
                    notaEditando = null
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { notaEditando = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
