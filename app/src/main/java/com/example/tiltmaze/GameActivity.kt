package com.example.tiltmaze

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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.example.tiltmaze.ui.theme.TiltMazeGameTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameActivity : ComponentActivity() {

    private lateinit var preferenceStorage: PreferenceStorage
    private var ballColor by mutableStateOf(BallColor.RED)
    private var confirmShowTimer by mutableStateOf(true)
    private var ballSpeed: Int = 3

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock orientation to portrait during game
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Initialize preferences
        preferenceStorage = PreferenceStorage(applicationContext)

        // Load preferences and then set content
        lifecycleScope.launch {
            val prefs = preferenceStorage.appPreferencesFlow.first()
            ballColor = prefs.ballColor
            confirmShowTimer = prefs.confirmShowTimer
            ballSpeed = prefs.ballSpeed

            setContent {
                TiltMazeGameTheme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        GameUI(ballColor = ballColor, confirmShowTimer = confirmShowTimer, ballSpeed = ballSpeed)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Allow screen rotation again after leaving the game
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun GameUI(ballColor: BallColor, confirmShowTimer: Boolean, ballSpeed: Int) {
    var timerText by remember { mutableStateOf("Time: 0.000s") }
    var tiltX by remember { mutableStateOf(0f) }
    var tiltY by remember { mutableStateOf(0f) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        accelerometerFlow(context).collect { event ->
            tiltX = -event.values[0]
            tiltY = event.values[1]
        }
    }

    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            val elapsedMillis = System.currentTimeMillis() - startTime
            val seconds = elapsedMillis / 1000
            val ms = elapsedMillis % 1000
            timerText = "Time: $seconds.${ms.toString().padStart(3, '0')}s"
            delay(50)
        }
    }

    GameScreen(
        timerText = timerText,
        tiltX = tiltX,
        tiltY = tiltY,
        ballColor = ballColor,
        confirmShowTimer = confirmShowTimer,
        ballSpeed = ballSpeed,
        onBackToMenu = {
            context.startActivity(Intent(context, MainMenuActivity::class.java))
            (context as? ComponentActivity)?.finish()
        },
        onMazeWin = { elapsedMillis ->
            val winTime = "${elapsedMillis / 1000}.${(elapsedMillis % 1000).toString().padStart(3, '0')}s"
            context.startActivity(Intent(context, WinActivity::class.java).apply {
                putExtra("TIME_TAKEN", winTime)
            })
            (context as? ComponentActivity)?.finish()
        }
    )
}



