package com.example.tiltmaze

import android.content.Context
import android.hardware.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

//This was a ChatGPT suggestion for handling the accelerometer data in a more lifecycle friendly manner
fun accelerometerFlow(context: Context): Flow<SensorEvent> = callbackFlow {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let { trySend(it).isSuccess }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)

    awaitClose {
        sensorManager.unregisterListener(listener)
    }
}
