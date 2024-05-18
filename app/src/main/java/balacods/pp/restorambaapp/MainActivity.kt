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
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.databinding.ActivityMainBinding
import balacods.pp.restorambaapp.databinding.ContentBaseBinding
import balacods.pp.restorambaapp.shakeDetector.ShakeDetectionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.util.stream.Collectors


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentBaseBinding
    private lateinit var bindingActivity: ActivityMainBinding

    private lateinit var restorambaApiService: RestorambaApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingActivity.root)

        restorambaApiService = Common.retrofitService

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
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<List<MenuData>> = restorambaApiService.getRandomDish()
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("detail")
            }
            val responseRest: Response<List<RestaurantData>> = restorambaApiService.getListRestaurants()
            val messageRest = responseRest.errorBody()?.string()?.let {
                JSONObject(it).getString("detail")
            }
            withContext(Dispatchers.Main) {
                if (message.equals(null) && messageRest.equals(null)) {
                    val menuData: MenuData = response.body()!![0]
                    val mapRest: Map<Long, String> = responseRest.body()!!.stream()
                        .collect(Collectors.toMap(RestaurantData::customerId, RestaurantData::restaurantName))
                    bindingActivity.apply {
                        idShake.apply {
                            tvTitleDish.text = menuData.dishName
                            tvTitleRestaurant.text = mapRest.getOrDefault(menuData.restaurantId, null)
                            idSumDish.text = String.format("Цена: %s руб", menuData.dishPrice)
                        }
                    }
                }
            }
        }
        bindingActivity.idShake.shakePopUp.visibility = View.VISIBLE
    }

    private fun init() {
        initSwipe()
        initBt()
    }

    private fun initBt() {

        // Pop up 1 start
        bindingActivity.idShake.btClose.setOnClickListener {
            bindingActivity.idShake.shakePopUp.visibility = View.GONE
        }

        bindingActivity.idShake.idButtonContinue.setOnClickListener {
            // Тут рандом блюдо из бэка
            // Думаем как брать рандом из ближайших ресторанов
        }
        // end

        // Pop up 2
        bindingActivity.idShake.idButtonMoreDetails.setOnClickListener {
            bindingActivity.idShake.shakePopUp.visibility = View.GONE
            bindingActivity.idShake2.shakePopUp2.visibility = View.VISIBLE // Не заверстан
        }
        bindingActivity.idShake2.btClose.setOnClickListener {
            bindingActivity.idShake2.shakePopUp2.visibility = View.GONE
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