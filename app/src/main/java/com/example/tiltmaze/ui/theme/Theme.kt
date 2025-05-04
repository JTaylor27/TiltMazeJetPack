package com.example.tiltmaze.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    secondary = AccentColor,
    onSecondary = Color.Black,
    background = BackgroundColor,
    surface = Color.White,
    onBackground = Color.Black
)

@Composable
fun TiltMazeGameTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

