package balacods.pp.restorambaapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import balacods.pp.restorambaapp.databinding.ActivityMainBinding
import balacods.pp.restorambaapp.databinding.ContentBaseBinding
import balacods.pp.restorambaapp.shakeDetector.ShakeDetectionService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentBaseBinding
    private lateinit var bindingActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingActivity.root)

        // здесь сервис по обработки тряски телефона
        val shakeDetectionServiceIntent = Intent(this, ShakeDetectionService::class.java)
        ContextCompat.startForegroundService(this, shakeDetectionServiceIntent)

        // Регистрация BroadcastReceiver для получения сообщений от LocalBroadcastManager
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(shakeReceiver, IntentFilter("shake_event"));

        init()
    }

    private val shakeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "shake_event") {
                // Вызываете нужный метод в Activity
                showShake()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Отмена регистрации BroadcastReceiver для предотвращения утечек памяти
        LocalBroadcastManager.getInstance(this).unregisterReceiver(shakeReceiver)
    }

    fun showShake() {
        bindingActivity.idShake.shakePopUp.visibility = View.VISIBLE
        bindingActivity.include.root.visibility = View.GONE
    }

    private fun init() {
        initSwipe()
        initBt()
    }

    private fun initBt() {

        // Pop up 1
        bindingActivity.idShake.btClose.setOnClickListener {
            bindingActivity.idShake.shakePopUp.visibility = View.GONE
            bindingActivity.include.root.visibility = View.VISIBLE
        }

        // Pop up 2
        bindingActivity.idShake.btContinue.setOnClickListener {
            bindingActivity.idShake.shakePopUp.visibility = View.GONE
            bindingActivity.idShake2.shakePopUp2.visibility = View.VISIBLE
        }
        bindingActivity.idShake2.btClose.setOnClickListener {
            bindingActivity.idShake2.shakePopUp2.visibility = View.GONE
            bindingActivity.include.root.visibility = View.VISIBLE
        }
    }

    private fun initSwipe() {
        // Здесь код для свайпа, но пока все сыро((

//        val viewPager = bindingActivity.viewPager
//        val tabLayout = bindingActivity.tabLayout
//
//        viewPager.adapter = ViewPagerAdapter(this)
//
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            when (position) {
//                0 -> tab.text = "First"
//                1 -> tab.text = "Second"
//                else -> tab.text = "Third"
//            }
//        }.attach()
//
//        val navController = findNavController(R.id.nav_host_fragment_content_base)
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                tab?.let {
//                    when (tab.position) {
//                        0 -> navController.navigate(R.id.mainFrag)
//                        1 -> navController.navigate(R.id.searchFrag)
//                        2 -> navController.navigate(R.id.restaurantsFrag)
//                        else -> throw IllegalArgumentException()
//                    }
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {}
//        })
    }
}