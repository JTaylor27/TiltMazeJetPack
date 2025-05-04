package com.example.tiltmaze

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.*


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM) //for generateMaze (quick fix)
@Composable
fun MazeCanvas(
    modifier: Modifier = Modifier,
    rows: Int,
    cols: Int,
    tileSizeDp: Dp,
    tiltX: Float,
    tiltY: Float,
    onGameWin: (Long) -> Unit
) {
    val tileSizePx = with(LocalDensity.current) { tileSizeDp.toPx() }

    val mazeState = remember { generateMaze(rows, cols) }
    val maze = mazeState.maze
    val goal = mazeState.goal

    var ballOffset by remember { mutableStateOf(Offset(1.5f, 1.5f)) }
    val startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var gameWon by remember { mutableStateOf(false) }

    Canvas(modifier = modifier.background(Color.Black)) {
        // --- Draw maze ---
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val left = col * tileSizePx
                val top = row * tileSizePx
                val color = when {
                    row == goal.first && col == goal.second -> Color.Green
                    maze[row][col] == 1 -> Color.Black
                    else -> Color.DarkGray
                }
                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(tileSizePx, tileSizePx),
                    cornerRadius = CornerRadius(2f, 2f)
                )
            }
        }

        // --- Ball movement ---
        val speed = 0.01f
        val nextX = ballOffset.x + tiltX * speed
        val nextY = ballOffset.y + tiltY * speed
        val radius = 0.35f

        val canMoveX = canMove(ballOffset.y, nextX, radius, maze, rows, cols)
        val canMoveY = canMove(nextY, ballOffset.x, radius, maze, rows, cols)

        ballOffset = Offset(
            x = if (canMoveX) nextX.coerceIn(radius, cols - radius) else ballOffset.x,
            y = if (canMoveY) nextY.coerceIn(radius, rows - radius) else ballOffset.y
        )

        val cx = ballOffset.x * tileSizePx
        val cy = ballOffset.y * tileSizePx
        drawCircle(
            color = Color.Red,
            radius = tileSizePx * radius,
            center = Offset(cx, cy)
        )

        val currentRow = ballOffset.y.toInt()
        val currentCol = ballOffset.x.toInt()
        if (!gameWon && currentRow == goal.first && currentCol == goal.second) {
            gameWon = true
            onGameWin(System.currentTimeMillis() - startTime)
        }
    }
}

// --- Helper function to check if the ball can move to a new position ---
fun canMove(nextRow: Float, nextCol: Float, radius: Float, maze: Array<IntArray>, rows: Int, cols: Int): Boolean {
    // Convert float positions to grid indices
    val rowIdx = nextRow.toInt()
    val colIdx = nextCol.toInt()

    // Check if the new position is within bounds and not a wall
    if (rowIdx in 0 until rows && colIdx in 0 until cols) {
        return maze[rowIdx][colIdx] != 1 // 1 indicates a wall
    }
    return false
}

