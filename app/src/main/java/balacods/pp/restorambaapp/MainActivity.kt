package balacods.pp.restorambaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import balacods.pp.restorambaapp.databinding.ContentBaseBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}