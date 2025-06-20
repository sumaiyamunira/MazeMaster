package com.sumaiyamunira.mazemaster.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import com.sumaiyamunira.mazemaster.domain.model.Maze

// presentation/ui/MazeCanvas.kt
@Composable
fun MazeCanvas(maze: Maze, player: Pair<Int, Int>, color:Color) {
    val cellSize = 60f // Fixed size for each cell

    // The Canvas will fill its available width and maintain a 1:1 aspect ratio
    Canvas(modifier = Modifier
        .fillMaxWidth() // Fill the width available to it
    ) {
        val mazeWidth = maze.cols * cellSize
        val mazeHeight = maze.rows * cellSize

        // Calculate offsets to center the maze drawing within the actual Canvas bounds.
        // This is crucial if the Canvas's drawing area (size.width, size.height)
        // is larger than the actual maze's calculated pixel dimensions (mazeWidth, mazeHeight).
        val offsetX = (size.width - mazeWidth) / 2
        val offsetY = (size.height - mazeHeight) / 2

        // Draw maze walls
        for (i in 0 until maze.rows) {
            for (j in 0 until maze.cols) {
                // Apply offsets to each cell's drawing coordinates
                val x = j * cellSize + offsetX
                val y = i * cellSize + offsetY
                val cell = maze.grid[i][j]

                // Draw walls for the current cell
                if (cell.topWall)
                    drawLine(Color.Black, Offset(x, y), Offset(x + cellSize, y), 4f)
                if (cell.leftWall)
                    drawLine(Color.Black, Offset(x, y), Offset(x, y + cellSize), 4f)
                if (cell.rightWall)
                    drawLine(Color.Black, Offset(x + cellSize, y), Offset(x + cellSize, y + cellSize), 4f)
                if (cell.bottomWall)
                    drawLine(Color.Black, Offset(x, y + cellSize), Offset(x + cellSize, y + cellSize), 4f)
            }
        }

        // Draw player circle
        val (px, py) = player
        drawCircle(
            color = color,
            radius = cellSize / 3,
            // Apply offsets to player position as well
            center = Offset(py * cellSize + cellSize / 2 + offsetX, px * cellSize + cellSize / 2 + offsetY)
        )
// 🎯 Draw target flag at bottom-right cell
        val targetCellX = (maze.cols - 1) * cellSize + offsetX
        val targetCellY = (maze.rows - 1) * cellSize + offsetY

// Flag pole (line)
        drawLine(
            color = Color.DarkGray,
            start = Offset(targetCellX + cellSize / 2, targetCellY + cellSize / 4),
            end = Offset(targetCellX + cellSize / 2, targetCellY + cellSize / 1.2f),
            strokeWidth = 5f
        )

// Flag (rectangle)
        drawRect(
            color = Color.Magenta,
            topLeft = Offset(targetCellX + cellSize / 2, targetCellY + cellSize / 4),
            size = androidx.compose.ui.geometry.Size(cellSize / 3, cellSize / 4)
        )
    }
}