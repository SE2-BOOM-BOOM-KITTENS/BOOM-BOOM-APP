package com.example.boomboomfrontend.game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector (private val context: Context, private val onShakeDetected: () -> Unit) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    init {
        accelerometer?.let{
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        } ?: run{
            println("Kein Beschleunigungssensor verfügbar")
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && isShakeDetected(event)){
                onShakeDetected() // wenn schütteln erkannt wird, ruf die Callback-Methode auf

        }
    }

    private fun isShakeDetected (event: SensorEvent): Boolean{
        // Logik zum Erkennen des Schüttelns
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val acceleration = sqrt((x * x + y * y + z * z).toDouble())
        return acceleration > SHAKE_THRESHOLD
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){
        //empty?
    }

    companion object{
        private const val SHAKE_THRESHOLD = 12.0 // Schwellenwert für das Erkennen des Schüttelns
    }


}