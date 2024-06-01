package balacods.pp.restorambaapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import balacods.pp.restorambaapp.app.OnDataPassListener
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.enum.StatusCodeShakeRequest
import balacods.pp.restorambaapp.data.model.DishAndPhotoData
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.databinding.ActivityMainBinding
import balacods.pp.restorambaapp.databinding.ContentBaseBinding
import balacods.pp.restorambaapp.shakeDetector.ShakeDetectionService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import java.util.stream.Collectors


class MainActivity : AppCompatActivity(), OnDataPassListener {

    private lateinit var binding: ContentBaseBinding
    private lateinit var bindingActivity: ActivityMainBinding

    private lateinit var restorambaApiService: RestorambaApiService
    private var code: String = ""

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
            Log.i("shakeReceiver", intent.action.toString())
            if (intent.action == "shake_event") {
                Log.i("shakeReceiver", code)
                // Действия после получения данных
//                showShake()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Отмена регистрации BroadcastReceiver для предотвращения утечек памяти
        LocalBroadcastManager.getInstance(this).unregisterReceiver(shakeReceiver)
    }

    override fun onDataPass(data: String?) {
        code = data!!
    }

    private fun showShake() {
        if (code == StatusCodeShakeRequest.All.code) {
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<DishAndPhotoData> = restorambaApiService.getRandomDish()
                val message = response.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseRest: Response<List<RestaurantAndPhotoData>> =
                    restorambaApiService.getListRestaurants()
                val messageRest = responseRest.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                withContext(Dispatchers.Main) {
                    if (message.equals(null) && messageRest.equals(null)) {
                        val menuData: DishAndPhotoData = response.body()!!
                        val mapRest: Map<Long, String> = responseRest.body()!!.stream()
                            .map { x -> x.restaurant }
                            .collect(
                                Collectors.toMap(
                                    RestaurantData::customerId,
                                    RestaurantData::restaurantName
                                )
                            )
                        bindingActivity.apply {
                            idShake.apply {

                                if (menuData.photo != null) {
                                    imPhotoIcon.visibility = View.GONE
                                    Glide.with(applicationContext)
                                        .load(menuData.photo.link1)
                                        .centerCrop()
                                        .transform(RoundedCorners(20))
                                        .error(R.drawable.ic_launcher_foreground)
                                        .placeholder(R.drawable.ic_launcher_foreground)
                                        .into(imPhoto)
                                }

                                tvTitleDish.text = menuData.dish.dishName
                                tvTitleRestaurant.text =
                                    mapRest.getOrDefault(menuData.dish.restaurantId, null)
                                idSumDish.text =
                                    String.format("Цена: %s руб", menuData.dish.dishPrice)
                            }
                            idShake2.apply {
                                tvText.text = menuData.dish.dishDescription
                            }
                        }
                    }
                }
            }
        } else if (code.split(':')[0] == StatusCodeShakeRequest.ONLYONERESTAURANT.code) {
            val restaurantId: Long = code.split(':')[1].toLong()
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<DishAndPhotoData> =
                    restorambaApiService.getRandomDishByRestaurantId(restaurantId)
                val message = response.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseRest: Response<List<RestaurantAndPhotoData>> =
                    restorambaApiService.getListRestaurants()
                val messageRest = responseRest.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                withContext(Dispatchers.Main) {
                    if (message.equals(null) && messageRest.equals(null)) {
                        val menuData: DishAndPhotoData = response.body()!!
                        val mapRest: Map<Long, String> = responseRest.body()!!.stream()
                            .map { x -> x.restaurant }
                            .collect(
                                Collectors.toMap(
                                    RestaurantData::customerId,
                                    RestaurantData::restaurantName
                                )
                            )
                        bindingActivity.apply {
                            idShake.apply {
                                tvTitleDish.text = menuData.dish.dishName
                                tvTitleRestaurant.text =
                                    mapRest.getOrDefault(menuData.dish.restaurantId, null)
                                idSumDish.text =
                                    String.format("Цена: %s руб", menuData.dish.dishPrice)
                            }
                            idShake2.apply {
                                tvText.text = menuData.dish.dishDescription
                            }
                        }
                    }
                }
            }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<DishAndPhotoData> = restorambaApiService.getRandomDish()
                val message = response.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseRest: Response<List<RestaurantAndPhotoData>> =
                    restorambaApiService.getListRestaurants()
                val messageRest = responseRest.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                withContext(Dispatchers.Main) {
                    if (message.equals(null) && messageRest.equals(null)) {
                        val menuData: DishAndPhotoData = response.body()!!
                        val mapRest: Map<Long, String> = responseRest.body()!!.stream()
                            .map { x -> x.restaurant }
                            .collect(
                                Collectors.toMap(
                                    RestaurantData::customerId,
                                    RestaurantData::restaurantName
                                )
                            )
                        bindingActivity.apply {
                            idShake.apply {
                                tvTitleDish.text = menuData.dish.dishName
                                tvTitleRestaurant.text =
                                    mapRest.getOrDefault(menuData.dish.restaurantId, null)
                                idSumDish.text =
                                    String.format("Цена: %s руб", menuData.dish.dishPrice)
                            }
                            idShake2.apply {
                                tvText.text = menuData.dish.dishDescription
                            }
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
            code = ""
            bindingActivity.idShake.shakePopUp.visibility = View.GONE
        }

        bindingActivity.idShake.idButtonContinue.setOnClickListener {
            showShake()
        }
        // end

        // Pop up 2
        bindingActivity.idShake.idButtonMoreDetails.setOnClickListener {
            bindingActivity.apply {
                idShake.shakePopUp.visibility = View.GONE
                idShake2.shakePopUp2.visibility = View.VISIBLE // Не заверстан
            }
        }
        bindingActivity.idShake2.btClose.setOnClickListener {
            code = ""
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