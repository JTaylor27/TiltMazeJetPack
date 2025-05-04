package com.example.tiltmaze

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AboutScreen(onBackToMenu: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tilt Maze\n\nThis game uses tilt sensors to allow the player to navigate a ball through a maze. The aim is to reach the goal in as little time as possible.\n\nCreated for Android App Development Class\n\nBy Josh Taylor",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(onClick = onBackToMenu) {
            Text("Back to Menu")
        }
    }
}
