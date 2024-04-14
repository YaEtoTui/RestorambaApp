package balacods.pp.restorambaapp.shakeDetector

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.pow
import kotlin.math.sqrt

/*
* Класс, в котором происходит обнаружения тряски телефона.
* @SHAKE_THRESHOLD - параметр который отвечает за чувствительность тряски
* */
class ShakeDetector() : SensorEventListener {

    private var onShakeListener: OnShakeListener? = null
    private var lastShakeTimestamp: Long = 0

    companion object {
        private const val SHAKE_THRESHOLD = 53.0f
        private const val SHAKE_TIME_INTERVAL = 3000 // Время в миллисекундах между потрясениями
    }

    interface OnShakeListener {
        fun onShake()
    }

    fun setOnShakeListener(onShakeListener: OnShakeListener?) {
        this.onShakeListener = onShakeListener
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastShakeTimestamp) > SHAKE_TIME_INTERVAL) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = sqrt(
                    x.toDouble().pow(2.0) + y.toDouble().pow(2.0) + z.toDouble().pow(2.0)
                ) - SensorManager.GRAVITY_EARTH
                if (acceleration > SHAKE_THRESHOLD) {
                    lastShakeTimestamp = currentTime
                    onShakeListener?.onShake()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Пустой метод, не используется
    }
}