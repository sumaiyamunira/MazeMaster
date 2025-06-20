package com.sumaiyamunira.mazemaster.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DirectionControls(onMove: (dx: Int, dy: Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { onMove(-1, 0) }) { Text("↑") }
        Row {
            Button(onClick = { onMove(0, -1) }) { Text("←") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { onMove(0, 1) }) { Text("→") }
        }
        Button(onClick = { onMove(1, 0) }) { Text("↓") }
    }
}