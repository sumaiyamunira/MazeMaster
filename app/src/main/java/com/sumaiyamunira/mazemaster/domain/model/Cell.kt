package com.sumaiyamunira.mazemaster.domain.model

data class Cell(
    var visited: Boolean = false,
    var topWall: Boolean = true,
    var bottomWall: Boolean = true,
    var leftWall: Boolean = true,
    var rightWall: Boolean = true
)