package com.example.tiltmaze

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiltmaze.ui.theme.TiltMazeGameTheme

class WinActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiltMazeGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {

                    // Get the time passed from the intent
                    val timeTaken = intent.getStringExtra("TIME_TAKEN") ?: "00:00"

                    // Call the composable that renders the Win screen
                    WinScreen(
                        elapsedTime = timeTaken,
                        onPlayAgain = {
                            // Start the GameActivity again
                            val intent = Intent(this, GameActivity::class.java)
                            startActivity(intent)
                            finish() // Finish WinActivity to prevent going back to it
                        },
                        onBackToMenu = {
                            // Go back to the MainMenuActivity
                            val intent = Intent(this, MainMenuActivity::class.java)
                            startActivity(intent)
                            finish() // Finish WinActivity to prevent going back to it
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WinScreen(
    elapsedTime: String, // Time passed since the game win
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title (Win message)
        Text(
            text = "You Win!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display elapsed time
        Text(
            text = "Time: $elapsedTime",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Play Again button
        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Play Again")
        }

        // Back to Menu button
        Button(
            onClick = onBackToMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Back to Menu")
        }
    }
}

// Preview function for testing purposes
@Preview(showBackground = true)
@Composable
fun WinScreenPreview() {
    WinScreen(elapsedTime = "05:32", onPlayAgain = {}, onBackToMenu = {})
}
