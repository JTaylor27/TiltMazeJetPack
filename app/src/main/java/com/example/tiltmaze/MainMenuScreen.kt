package com.example.tiltmaze

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen() {
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tilt Maze",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                context.startActivity(Intent(context, GameActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
            ) {
            Text("Play")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, AboutActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
            ) {
            Text("About")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .fillMaxWidth()
            ) {
            Text("Exit")
        }
    }
}

