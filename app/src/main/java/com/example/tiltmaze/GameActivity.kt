package com.example.tiltmaze

import android.R.color
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.tiltmaze.ui.theme.TiltMazeGameTheme
import kotlinx.coroutines.delay


class GameActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM) //for GameScreen (quick fix)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ChatGPT Prompt: "How to keep the game screen from rotating during game play"
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            TiltMazeGameTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    var timerText by remember { mutableStateOf("Time: 0.000s") }
                    var tiltX by remember { mutableStateOf(0f) }
                    var tiltY by remember { mutableStateOf(0f) }

                    val scope = rememberCoroutineScope()

                    // Collect accelerometer flow in a lifecycle-aware manner
                    LaunchedEffect(Unit) {
                        // Launch the sensor flow in the lifecycle
                        accelerometerFlow(applicationContext).collect { event ->
                            tiltX = -event.values[0]  // X axis tilt
                            tiltY = event.values[1]   // Y axis tilt
                        }
                    }

                    // Timer logic for updating the time as the game runs
                    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

                    LaunchedEffect(Unit) {
                        while (true) {
                            val elapsedMillis = System.currentTimeMillis() - startTime
                            val seconds = elapsedMillis / 1000
                            val ms = elapsedMillis % 1000
                            timerText = "Time: $seconds.${ms.toString().padStart(3, '0')}s"
                            delay(50) // Update every 50 milliseconds for smooth timer display
                        }
                    }

                    GameScreen(
                        timerText = timerText,
                        tiltX = tiltX,
                        tiltY = tiltY,
                        onBackToMenu = {
                            startActivity(Intent(this@GameActivity, MainMenuActivity::class.java))
                            finish()
                        },
                        onMazeWin = { elapsedMillis ->
                            // Logic for handling maze win
                            val seconds = elapsedMillis / 1000
                            val ms = elapsedMillis % 1000
                            val winTime = "$seconds.${ms.toString().padStart(3, '0')}s"
                            startActivity(Intent(this@GameActivity, WinActivity::class.java).apply {
                                putExtra("TIME_TAKEN", winTime)
                            })
                            finish() // Finish GameActivity to prevent returning here
                        }
                    )
                }
            }
        }
    }

    //ChatGPT(same prompt from above) - allows the screen to be rotated again after the game activity is done
    override fun onDestroy() {
        super.onDestroy()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
