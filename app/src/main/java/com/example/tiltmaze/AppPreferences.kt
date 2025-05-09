package com.example.tiltmaze

enum class  BallColor(val text: String) {
    RED("Red"),
    BLUE("Blue"),
    GREEN("Green")
}

class AppPreferences (
    val ballColor: BallColor = BallColor.RED,
    val confirmShowTimer: Boolean = true,
    val ballSpeed: Int = 3
)