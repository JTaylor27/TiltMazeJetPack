package com.example.tiltmaze

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tiltmaze.AppPreferences
import com.example.tiltmaze.PreferenceStorage
import com.example.tiltmaze.BallColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onUpClick: () -> Unit = {}
) {
    val store = PreferenceStorage(LocalContext.current)
    val appPrefs = store.appPreferencesFlow.collectAsStateWithLifecycle(AppPreferences())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // TODO: Add preference composables here
            val colorOptions = BallColor.entries.map { it.text }
            val selectedIndex = appPrefs.value.ballColor.ordinal

            ListSetting(
                title = "Ball color",
                values = colorOptions,
                selectedIndex = selectedIndex,
                onItemClick = { index ->
                    coroutineScope.launch  {
                        val selectedColor = BallColor.entries[index]
                        store.saveBallColor(selectedColor)
                    }
                }
            )
            SwitchSetting(
                title = "Show Timer",
                checked = appPrefs.value.confirmShowTimer,
                onCheckedChange = { checked ->
                    coroutineScope.launch {
                        store.saveConfirmShowTimer(checked)
                    }
                }
            )
            SliderSetting(
                title = "Ball Speed",
                initValue = appPrefs.value.ballSpeed,
                valueRange = 1..5,
                onValueChangeFinished = { finalValue ->
                    coroutineScope.launch {
                        store.saveBallSpeed(finalValue)
                    }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSetting(
    title: String,
    values: List<String>,
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 20.sp
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            TextField(
                value = values[selectedIndex],
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = modifier
                    .menuAnchor()
                    .width(200.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                values.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            expanded = false
                            onItemClick(index)
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun SwitchSetting(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 20.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
@Composable
fun SliderSetting(
    title: String,
    initValue: Int,
    valueRange: ClosedRange<Int>,
    onValueChangeFinished: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember { mutableIntStateOf(initValue) }

    Column {
        Text(
            text = title,
            fontSize = 20.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Slider(
                value = sliderPosition.toFloat(),
                steps = valueRange.endInclusive - valueRange.start - 1,
                valueRange = valueRange.start.toFloat()..valueRange.endInclusive.toFloat(),
                onValueChange = { sliderPosition = it.roundToInt() },
                onValueChangeFinished = {
                    onValueChangeFinished(sliderPosition)
                },
                modifier = modifier.weight(1f)
            )
            Text(sliderPosition.toString())
        }
    }

    // Set initial slider position when initValue changes
    LaunchedEffect(initValue) {
        sliderPosition = initValue
    }
}