package balacods.pp.restorambaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

//    private fun showText(text: String) {
//        val str: TextView = findViewById(R.id.textView)
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//        str.visibility = View.VISIBLE
//        Thread.sleep(10000000)
//    }
}