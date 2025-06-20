package com.sumaiyamunira.mazemaster

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sumaiyamunira.mazemaster.presentation.viewmodel.theme.MazeMasterTheme
import com.sumaiyamunira.mazemaster.ui.MazeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MazeMasterTheme {
                MazeScreen()
                WelcomeMessage(this, "MazeMaster")
            }
        }
    }
}

fun WelcomeMessage(context: Context, name: String) {
    Toast.makeText(context, "Welcome to " + name, Toast.LENGTH_LONG).show()
}
