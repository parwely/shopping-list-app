package com.example.shoppinglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.ui.theme.ShoppingListAppTheme

// Haupt-Activity der App, Einstiegspunkt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Aktiviert Edge-to-Edge-Layout für modernes UI
        setContent {
            // Setzt das Theme für die App
            ShoppingListAppTheme {
                // Oberflächen-Container mit Hintergrundfarbe
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Scaffold sorgt für Grundlayout (z.B. Padding für Systemleisten)
                    Scaffold { innerPadding ->
                        // Startet die Haupt-Composable der App
                        ShoppingListApp(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

/**
 * Hauptkomponente der Einkaufslisten-App
 */
@Composable
fun ShoppingListApp(modifier: Modifier = Modifier) {
    // State für die Einkaufsliste (mutable, damit UI sich aktualisiert)
    val shoppingItems = remember { mutableStateListOf<String>() }
    // State für den aktuellen Text im Eingabefeld
    var newItemText by remember { mutableStateOf("") }

    // Farben für Karten und Akzente aus dem Theme
    val cardBackground = MaterialTheme.colorScheme.surface
    val accentColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp) // Außenabstand für die ganze App
    ) {
        // Titelkarte der App
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = accentColor.copy(alpha = 0.1f) // Leichter Akzent
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Meine Einkaufsliste",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                color = accentColor
            )
        }

        // Eingabebereich für neue Artikel
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardBackground
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Texteingabefeld für neuen Artikel
                OutlinedTextField(
                    value = newItemText,
                    onValueChange = { newItemText = it }, // Aktualisiert State bei Eingabe
                    label = { Text("Was brauchst du?") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f)
                    )
                )

                // Button zum Hinzufügen eines neuen Artikels
                Button(
                    onClick = {
                        if (newItemText.isNotBlank()) { // Nur wenn Eingabe nicht leer
                            shoppingItems.add(newItemText) // Artikel zur Liste hinzufügen
                            newItemText = "" // Eingabefeld leeren
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Hinzufügen")
                }
            }
        }

        // Karte für die Liste der Einkaufsartikel
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Nimmt den restlichen Platz ein
            colors = CardDefaults.cardColors(
                containerColor = cardBackground
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Wenn die Liste leer ist, zeige einen Hinweistext
                if (shoppingItems.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Deine Einkaufsliste ist leer",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Füge neue Artikel hinzu, um zu beginnen",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                } else {
                    // Ansonsten zeige die Liste der Artikel
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        // Für jeden Artikel in der Liste
                        items(shoppingItems) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = item,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    trailingContent = {
                                        // Button zum Entfernen des Artikels
                                        IconButton(
                                            onClick = { shoppingItems.remove(item) }
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Artikel entfernen",
                                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Vorschau der Einkaufslisten-App im Design-Editor
 */
@Preview(showBackground = true)
@Composable
fun ShoppingListAppPreview() {
    ShoppingListAppTheme {
        ShoppingListApp()
    }
}