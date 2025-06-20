package com.sumaiyamunira.mazemaster.domain.usecase

import com.sumaiyamunira.mazemaster.domain.model.Cell
import com.sumaiyamunira.mazemaster.domain.model.Maze
import java.util.Stack

class GenerateMazeUseCase {
    operator fun invoke(rows: Int, cols: Int): Maze {
        val grid = Array(rows) { Array(cols) { Cell() } }
        val stack = Stack<Pair<Int, Int>>()
        stack.push(0 to 0)

        while (stack.isNotEmpty()) {
            val (x, y) = stack.peek()
            grid[x][y].visited = true

            val neighbors = mutableListOf<Pair<Int, Int>>()
            if (x > 0 && !grid[x - 1][y].visited) neighbors.add(x - 1 to y)
            if (x < rows - 1 && !grid[x + 1][y].visited) neighbors.add(x + 1 to y)
            if (y > 0 && !grid[x][y - 1].visited) neighbors.add(x to y - 1)
            if (y < cols - 1 && !grid[x][y + 1].visited) neighbors.add(x to y + 1)

            if (neighbors.isNotEmpty()) {
                val (nx, ny) = neighbors.random()
                when {
                    nx == x - 1 -> {
                        grid[x][y].topWall = false
                        grid[nx][ny].bottomWall = false
                    }
                    nx == x + 1 -> {
                        grid[x][y].bottomWall = false
                        grid[nx][ny].topWall = false
                    }
                    ny == y - 1 -> {
                        grid[x][y].leftWall = false
                        grid[nx][ny].rightWall = false
                    }
                    ny == y + 1 -> {
                        grid[x][y].rightWall = false
                        grid[nx][ny].leftWall = false
                    }
                }
                stack.push(nx to ny)
            } else {
                stack.pop()
            }
        }

        return Maze(rows, cols, grid)
    }
}
