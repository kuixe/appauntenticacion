package com.example.myapli

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapli.ui.theme.MyapliTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyapliTheme {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE) }
    var darkMode by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedFontSize by remember { mutableStateOf("Mediana") }
    val fontSizeOptions = listOf("Pequeña", "Mediana", "Grande")

    // Recuperar el color guardado en SharedPreferences
    var selectedColor by remember { mutableStateOf(sharedPreferences.getString("bg_color", "White") ?: "White") }
    val colorOptions = listOf("White", "Blue", "Green", "Yellow")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Configuraciones", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Modo Oscuro
        SettingItem(title = "Modo Oscuro") {
            Switch(checked = darkMode, onCheckedChange = { darkMode = it })
        }

        // Notificaciones
        SettingItem(title = "Notificaciones") {
            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
        }

        // Tamaño de Fuente
        SettingItem(title = "Tamaño de Fuente") {
            DropdownMenuSetting(
                selectedOption = selectedFontSize,
                options = fontSizeOptions,
                onOptionSelected = { selectedFontSize = it }
            )
        }

        // Selección de Color de Fondo
        SettingItem(title = "Color de fondo") {
            DropdownMenuSetting(
                selectedOption = selectedColor,
                options = colorOptions,
                onOptionSelected = {
                    selectedColor = it
                    sharedPreferences.edit().putString("bg_color", it).apply()
                }
            )
        }
    }
}

@Composable
fun SettingItem(title: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(1f))
        content()
    }
}

@Composable
fun DropdownMenuSetting(selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}

