package com.sumaiyamunira.mazemaster.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sumaiyamunira.mazemaster.domain.model.Maze
import com.sumaiyamunira.mazemaster.domain.usecase.GenerateMazeUseCase

class MazeViewModel(
    private val generateMazeUseCase: GenerateMazeUseCase = GenerateMazeUseCase()
) : ViewModel() {

    private val _maze = mutableStateOf<Maze?>(null)
    val maze = _maze

    val playerPosition = mutableStateOf(0 to 0)

    fun generateMaze(rows: Int, cols: Int) {
        _maze.value = generateMazeUseCase(rows, cols)
        playerPosition.value = 0 to 0
    }
    fun resetMaze() {
        playerPosition.value = Pair(0, 0) // Resets player position to start
    }

    fun movePlayer(dx: Int, dy: Int) {
        val (x, y) = playerPosition.value
        val newX = x + dx
        val newY = y + dy
        val m = _maze.value ?: return

        if (newX in 0 until m.rows && newY in 0 until m.cols) {
            val currentCell = m.grid[x][y]
            val canMove = when {
                dx == -1 -> !currentCell.topWall
                dx == 1 -> !currentCell.bottomWall
                dy == -1 -> !currentCell.leftWall
                dy == 1 -> !currentCell.rightWall
                else -> false
            }

            if (canMove) playerPosition.value = newX to newY
        }
    }
}
