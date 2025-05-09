package com.example.tiltmaze

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tiltmaze.TiltMazeApplication
import kotlinx.coroutines.launch

class SettingsViewModel (
    private val prefStorage: PreferenceStorage
) : ViewModel() {

    var ballColor by mutableStateOf(BallColor.RED)
    var confirmShowTimer by mutableStateOf(true)
    private var ballSpeed: Int = 3

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as TiltMazeApplication)
                SettingsViewModel(
                    PreferenceStorage(application.appContext)
                )
            }
        }
    }
    init {
        // Collect settings from persistent storage
        viewModelScope.launch {
            prefStorage.appPreferencesFlow.collect {
                ballColor = it.ballColor
                confirmShowTimer = it.confirmShowTimer
                ballSpeed = it.ballSpeed
            }
        }
    }
}