package com.sumaiyamunira.mazemaster.domain.model

data class Maze(
    val rows: Int,
    val cols: Int,
    val grid: Array<Array<Cell>>
)