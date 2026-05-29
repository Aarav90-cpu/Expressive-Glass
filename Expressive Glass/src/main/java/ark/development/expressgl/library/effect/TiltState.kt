package ark.development.expressgl.library.effect

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Represents the normalized tilt of the device.
 * x and y are typically in the range [-1f, 1f].
 */
data class DeviceTilt(val x: Float = 0f, val y: Float = 0f)

/**
 * Remembers the current device tilt using the Gravity sensor.
 * Automatically registers and unregisters the sensor listener.
 */
@Composable
fun rememberDeviceTilt(): State<DeviceTilt> {
    val context = LocalContext.current
    val tiltState = remember { mutableStateOf(DeviceTilt()) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    // Gravity values are in m/s^2, typically ranging from -9.81 to 9.81.
                    // We normalize them to roughly [-1f, 1f].
                    // In portrait: 
                    // x is positive when tilting right.
                    // y is positive when tilting up (top of phone towards you).
                    val rawX = it.values[0]
                    val rawY = it.values[1]
                    
                    val normalizedX = (rawX / 9.81f).coerceIn(-1f, 1f)
                    val normalizedY = (rawY / 9.81f).coerceIn(-1f, 1f)
                    
                    tiltState.value = DeviceTilt(x = normalizedX, y = normalizedY)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        gravitySensor?.let {
            sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    return tiltState
}
