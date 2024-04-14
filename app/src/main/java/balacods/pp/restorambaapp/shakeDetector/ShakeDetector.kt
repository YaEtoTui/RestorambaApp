package balacods.pp.restorambaapp.shakeDetector

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

/*
* Класс, в котором происходит обнаружения тряски телефона.
* @SHAKE_THRESHOLD - параметр который отвечает за чувствительность тряски
* */
class ShakeDetector() : SensorEventListener {

    private var onShakeListener: OnShakeListener? = null
    private var lastShakeTime: Long = 0

    private var shakeDuration: Long = 0
    private var lastTiltAngle: Float = 0f

    companion object {
        private const val SHAKE_THRESHOLD = 35.0f
        private const val SHAKE_TIME_INTERVAL = 5000 // Время в миллисекундах между потрясениями
        private const val MAX_TILT_ANGLE = 45.0f // Максимальный угол наклона для обнаружения потрясения
        private const val MIN_SHAKE_DURATION = 6000 // Минимальная длительность тряски в миллисекундах
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

            if ((currentTime - lastShakeTime) > SHAKE_TIME_INTERVAL) {

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val tiltAngle = abs(Math.toDegrees(atan2(x.toDouble(), sqrt(y.toDouble().pow(2.0) + z.toDouble().pow(2.0)))))
                val tiltAngleDiff = abs(tiltAngle - lastTiltAngle)
                lastTiltAngle = tiltAngle.toFloat()

                if (tiltAngleDiff <= MAX_TILT_ANGLE) {
                    val acceleration = sqrt(x.toDouble().pow(2.0) + y.toDouble().pow(2.0) + z.toDouble().pow(2.0)) - SensorManager.GRAVITY_EARTH
                    if (acceleration > SHAKE_THRESHOLD) {
                        shakeDuration = currentTime - lastShakeTime

                        if (shakeDuration >= MIN_SHAKE_DURATION) {
                            lastShakeTime = currentTime
                            if (onShakeListener != null) {
                                onShakeListener?.onShake();
                            }
                        }

                    } else {
                        shakeDuration = 0
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Пустой метод, не используется
    }
}