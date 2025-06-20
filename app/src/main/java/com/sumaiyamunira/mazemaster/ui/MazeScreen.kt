package com.sumaiyamunira.mazemaster.ui

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sumaiyamunira.mazemaster.presentation.viewmodel.MazeViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.compose.AsyncImage
import com.sumaiyamunira.mazemaster.R
import androidx.compose.ui.unit.sp


@Composable
fun MazeScreen(viewModel: MazeViewModel = viewModel()) {
    val maze = viewModel.maze.value
    val player = viewModel.playerPosition.value
    val context = LocalContext.current
    var showButton by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.Yellow) }
    var musicPlayed by remember { mutableStateOf(false) }
    var failMusicPlayed by remember { mutableStateOf(false) }

    var countdownTime by remember { mutableStateOf(30) }
    var failState by remember { mutableStateOf(false) }
    var successState by remember { mutableStateOf(false) }

    val reachedTarget =
        maze != null && player.first == maze.rows - 1 && player.second == maze.cols - 1

    // Timer logic
    LaunchedEffect(key1 = countdownTime, key2 = failState, key3 = successState) {
        if (!failState && !successState && countdownTime > 0) {
            kotlinx.coroutines.delay(1000L)
            countdownTime -= 1
        } else if (countdownTime == 0 && !reachedTarget) {
            failState = true
        }
    }

    // Play sound once on success
    if (reachedTarget && !musicPlayed) {
        LaunchedEffect(Unit) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.win2_music)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
            musicPlayed = true
            successState = true
        }
    }


// Play fail sound once on fail
    if (failState && !failMusicPlayed) {
        LaunchedEffect(Unit) {
            val mediaPlayer = MediaPlayer.create(context, R.raw.lose_music) // Your fail music file
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
            failMusicPlayed = true
        }
    }


    // Reset game after fail or success (for example after showing GIF for 3 seconds)
    LaunchedEffect(failState, successState) {
        if (failState || successState) {
            kotlinx.coroutines.delay(3000L)
            viewModel.resetMaze()  // implement this function to reset maze & player
            countdownTime = 60
            failState = false
            successState = false
            musicPlayed = false
            showButton = false
        }
    }

    // Use a Column to arrange elements vertically.
    // fillMaxSize() makes it take up all available space.
    // horizontalAlignment centers its direct children horizontally.
    // verticalArrangement.SpaceBetween pushes the top and bottom elements to the edges,
    // allowing the middle content (maze) to expand.
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // "Generate Maze" Button at the top
        Button(
            onClick = {
                viewModel.generateMaze(10, 10)
                showButton = true
                Toast.makeText(context, "Wish you all the best!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.padding(top = 16.dp) // Add some top padding
        ) {
            Text("Generate Maze")
        }

        // "Generate Maze" Button at the top
        if (showButton) {
            Row() {
                Spacer(modifier = Modifier.width(8.dp)) // ← Adds horizontal gap
                Button(
                    onClick = {
                        selectedColor = Color.Red
                    },
                    modifier = Modifier.padding(top = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red // 🔴 Set button background to red
                    )
                ) {
                    Text("")
                }
                Spacer(modifier = Modifier.width(8.dp)) // ← Adds horizontal gap
                Button(
                    onClick = {
                        selectedColor = Color.Yellow
                    },
                    modifier = Modifier.padding(top = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow // 🔴 Set button background to red
                    )
                ) {
                    Text("")
                }
                Spacer(modifier = Modifier.width(8.dp)) // ← Adds horizontal gap

                Button(
                    onClick = {
                        selectedColor = Color.Green
                    },
                    modifier = Modifier.padding(top = 5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green // 🔴 Set button background to red
                    )
                ) {
                    Text("")
                }
                Spacer(modifier = Modifier.width(8.dp)) // ← Adds horizontal gap
            }

            // Timer Text at the top
            Text(
                text = "Time left: $countdownTime s",
                modifier = Modifier.padding(top = 16.dp),
                color = if (countdownTime <= 10) Color.Red else Color.Black,
                fontSize = 32.sp // 👈 Increase the font size here
            )

        }


        // Spacer to push content apart when maze is not yet generated
        // This ensures the button remains at the top and the controls (if shown) at the bottom.
        Spacer(modifier = Modifier.height(16.dp))

        // Display the MazeCanvas and DirectionControls only if a maze exists
        maze?.let {
            Box(
                modifier = Modifier
                    .weight(1f)              // fills available vertical space
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                MazeCanvas(maze = it, player = player, selectedColor)
//
//                if (reachedTarget) {
//                    GifOverlay()
//                }

                if (failState) {
                    GifOverlay(true)
                }

                if (successState) {
                    GifOverlay(false)
                }
            }

            // Direction controls at the bottom center
            DirectionControls { dx, dy -> viewModel.movePlayer(dx, dy) }
        } ?: run {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

    @Composable
    fun GifOverlay(fail: Boolean) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()

        val model = if (fail) {
            "file:///android_asset/lose.gif"
        } else {
            "file:///android_asset/congrats.gif"
        }

        val contentDescription = if (fail) "Fail GIF" else "Victory GIF"

        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = Modifier.size(400.dp),  // Adjust size as needed
            imageLoader = imageLoader
        )
    }


