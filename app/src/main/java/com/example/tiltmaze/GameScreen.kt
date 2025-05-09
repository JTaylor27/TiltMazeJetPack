package com.example.tiltmaze

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM) //for MazeCanvas (quick fix)
@Composable
fun GameScreen(
    timerText: String,
    tiltX: Float,
    tiltY: Float,
    ballColor: BallColor,
    confirmShowTimer: Boolean,
    ballSpeed: Int,
    onBackToMenu: () -> Unit,
    onMazeWin: (Long) -> Unit
) {
    val rows = 11
    val cols = 11
    val tileSizeDp = 32.dp

    val mazeWidth = tileSizeDp * cols
    val mazeHeight = tileSizeDp * rows

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Timer
            if(confirmShowTimer) {
                Text(
                    text = timerText,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Maze
            Box(
                modifier = Modifier
                    .size(width = mazeWidth, height = mazeHeight),
                contentAlignment = Alignment.Center
            ) {
                MazeCanvas(
                    modifier = Modifier.fillMaxSize(),
                    rows = rows,
                    cols = cols,
                    tileSizeDp = tileSizeDp,
                    tiltX = tiltX,
                    tiltY = tiltY,
                    ballColor = ballColor,
                    ballSpeed = ballSpeed,
                    onGameWin = onMazeWin
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Back to Menu
            Button(
                onClick = onBackToMenu
            ) {
                Text("Back to Menu")
            }
        }
    }
}




