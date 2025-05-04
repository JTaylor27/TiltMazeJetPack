package com.example.tiltmaze

import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.random.Random

data class MazeState(
    val maze: Array<IntArray>,
    val goal: Pair<Int, Int>
)

//ChatGPT - "Random Maze generation logic"
@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM) //for .removeLast() (quick fix)
fun generateMaze(rows: Int, cols: Int): MazeState {
    val maze = Array(rows) { IntArray(cols) { 1 } }

    val directions = listOf(
        intArrayOf(-2, 0), intArrayOf(2, 0),
        intArrayOf(0, -2), intArrayOf(0, 2)
    )

    val stack = mutableListOf<Pair<Int, Int>>()
    stack.add(1 to 1)
    maze[1][1] = 0

    while (stack.isNotEmpty()) {
        val (r, c) = stack.last()
        val neighbors = directions.map { intArrayOf(r + it[0], c + it[1]) }
            .filter { it[0] in 1 until rows - 1 && it[1] in 1 until cols - 1 && maze[it[0]][it[1]] == 1 }

        if (neighbors.isNotEmpty()) {
            val (nr, nc) = neighbors.random()
            maze[nr][nc] = 0
            maze[(r + nr) / 2][(c + nc) / 2] = 0
            stack.add(nr to nc)
        } else {
            stack.removeLast()
        }
    }

    // Find bottom-right goal
    for (row in rows - 1 downTo 0) {
        for (col in cols - 1 downTo 0) {
            if (maze[row][col] == 0) {
                return MazeState(maze, row to col)
            }
        }
    }

    return MazeState(maze, rows - 1 to cols - 1)
}
