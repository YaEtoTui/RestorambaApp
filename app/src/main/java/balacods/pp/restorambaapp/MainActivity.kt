package balacods.pp.restorambaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import balacods.pp.restorambaapp.databinding.ContentBaseBinding
import balacods.pp.restorambaapp.shakeDetector.ShakeDetectionService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shakeDetectionServiceIntent = Intent(this, ShakeDetectionService::class.java)
        ContextCompat.startForegroundService(this, shakeDetectionServiceIntent)
    }

//    private fun showText(text: String) {
//        val str: TextView = findViewById(R.id.textView)
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//        str.visibility = View.VISIBLE
//        Thread.sleep(10000000)
//    }
}