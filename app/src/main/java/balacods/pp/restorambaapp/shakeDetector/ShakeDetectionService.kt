package balacods.pp.restorambaapp.shakeDetector

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import balacods.pp.restorambaapp.shakeDetector.ShakeDetector.OnShakeListener

class ShakeDetectionService : Service(), SensorEventListener {

    private val TAG: String = "ShakeDetectionService"
    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null
    private var shakeDetector: ShakeDetector? = null

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: ShakeDetectionService");

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?;
        accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        shakeDetector = ShakeDetector()
        shakeDetector!!.setOnShakeListener(object : OnShakeListener {
            override fun onShake() {
                Log.d(TAG, "onShake: Telephone shaken!")
                // Выполните здесь действия, которые должны происходить при тряске
                showText("Тряска")
            }
        })
    }

    //убрать костыль, так как надо будет походу работать с Activity, и пытаться этот pop up кидать во фрагменты, иначе мы не сможем работать с фрагментами((
    private fun showText(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ShakeDetectionService");

        if (sensorManager != null && accelerometerSensor != null) {
            sensorManager!!.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }

        // Запускаем сервис в переднем плане
        val notification = createNotification()
        startForeground(1, notification)

        return START_STICKY;
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ShakeDetectionService")
        if (sensorManager != null && accelerometerSensor != null) {
            sensorManager!!.unregisterListener(this)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        shakeDetector!!.onSensorChanged(event!!)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Пустой метод, не используется
    }

    private fun createNotification(): Notification {
        val channelId = "shake_detection_channel"
        val channelName = "Shake Detection Channel"
        val importance = NotificationManager.IMPORTANCE_LOW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
        val builder: Builder = Builder(this, channelId)
            .setContentTitle("Shake Detection Service")
            .setContentText("Running...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
        return builder.build()
    }
}