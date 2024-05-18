package balacods.pp.restorambaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.DishViewModel
import balacods.pp.restorambaapp.data.viewModel.RestaurantViewModel
import balacods.pp.restorambaapp.databinding.FragmentRestaurantBinding
import balacods.pp.restorambaapp.fragment.adapter.DishAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class RestaurantFragment : Fragment() {

    private lateinit var adapter: DishAdapter
    private lateinit var binding: FragmentRestaurantBinding

    private lateinit var restorambaApiService: RestorambaApiService
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private val dishViewModel: DishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorambaApiService = Common.retrofitService

        init()
    }

    private fun init() {
        initRetrofitData()
        initBtNav()
        initRcView()
        initBtPage()
    }

    private fun initRetrofitData() {
        restaurantViewModel.restaurantId.observe(viewLifecycleOwner) {restaurantId ->
            CoroutineScope(Dispatchers.IO).launch {
                val responseRestaurant: Response<List<RestaurantData>> = restorambaApiService.getRestaurantById(restaurantId)
                val messageRestaurant = responseRestaurant.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseMenu: Response<List<MenuData>> = restorambaApiService.getDishesByRestaurantId(restaurantId)
                val messageMenu = responseMenu.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                requireActivity().runOnUiThread {
                    if (messageRestaurant.equals(null)) {
                        val restaurantData: RestaurantData = responseRestaurant.body()!![0]
                        binding.apply {
                            idNameRestaurant.text = restaurantData.restaurantName
                            tvDesc.text = restaurantData.restaurantDescription
                        }
                    }
                    if (messageMenu.equals(null)) {
                        val menuDataList: List<MenuData> = responseMenu.body()!!
                        adapter.submitList(menuDataList)
                    }
                }
            }
        }
    }

    private fun initBtPage() {
        binding.idButtonGetRandomDish.setOnClickListener {
            // Отправка сообщения с помощью LocalBroadcastManager
            val intent = Intent("shake_event")
            LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)
            // А тут бэк, в котором зарандомим блюдо для своего ресторана, у которого нажмем кнопку
            // ...
        }
    }

    private fun initRcView() {
        adapter = DishAdapter()
        adapter.setOnButtonClickListener(object : DishAdapter.OnButtonClickListener {
            override fun onClick(dishId: Long, restaurantId: Long) {
                dishViewModel.ids.value = arrayOf(dishId, restaurantId)
                findNavController().navigate(R.id.action_restaurantFrag_to_dishFrag)
            }
        })
        binding.idListDishes.layoutManager = LinearLayoutManager(context)
        binding.idListDishes.adapter = adapter
    }

    private fun initBtNav() {
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_mainFrag)
        }
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_restaurantsFrag)
        }
    }
}