package balacods.pp.restorambaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.MenuAndNameRestaurantData
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.RestaurantAndDishViewModel
import balacods.pp.restorambaapp.data.viewModel.RestaurantViewModel
import balacods.pp.restorambaapp.databinding.FragmentSearchBinding
import balacods.pp.restorambaapp.fragment.adapter.DishSearchAdapter
import balacods.pp.restorambaapp.fragment.adapter.RestaurantSearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.stream.Collectors


class SearchFragment : Fragment() {

    private lateinit var adapterRestaurant: RestaurantSearchAdapter
    private lateinit var adapterDish: DishSearchAdapter
    private lateinit var binding: FragmentSearchBinding

    private var searchText: String = ""
    private var isRestaurant: Boolean = true
    private var listRestaurantsGlobal: List<RestaurantData> = emptyList()
    private var listDishesGlobal: List<MenuAndNameRestaurantData> = emptyList()

    private lateinit var restorambaApiService: RestorambaApiService
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private val restaurantAndDishViewModel: RestaurantAndDishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorambaApiService = Common.retrofitService

        init()
    }

    private fun init() {
        initSwitch()
        initSearch()
        initNav()
        initRcView()
        searchListRestaurantsAndMenu()
    }

    private fun searchListRestaurantsAndMenu() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseRestaurants: Response<List<RestaurantData>> = restorambaApiService.getListRestaurants()
            val messageRestaurants = responseRestaurants.errorBody()?.string()?.let {
                JSONObject(it).getString("detail")
            }
            val responseMenu: Response<List<MenuData>> = restorambaApiService.getAllDishes()
            val messageMenu = responseRestaurants.errorBody()?.string()?.let {
                JSONObject(it).getString("detail")
            }
            requireActivity().runOnUiThread {
                if (messageRestaurants.equals(null)) {
                    listRestaurantsGlobal = responseRestaurants.body()!!
                }
                if (messageMenu.equals(null)) {
                    val mapRestaurantData: Map<Long, String> = responseRestaurants.body()!!.stream()
                        .collect(Collectors.toMap(RestaurantData::customerId, RestaurantData::restaurantName))
                    val listDishes: List<MenuData> = responseMenu.body()!!
                    listDishesGlobal = listDishes.stream()
                        .map { dish -> MenuAndNameRestaurantData(
                            dish.dishesId,
                            dish.restaurantId,
                            mapRestaurantData.getOrDefault(dish.restaurantId, null)!!,
                            dish.dishName,
                            dish.dishDescription,
                            dish.dishPrice,
                            dish.dishWeight,
                            dish.dishType
                        )}
                        .collect(Collectors.toList())
                }
            }
        }
    }

    private fun initRcView() {
        adapterRestaurant = RestaurantSearchAdapter()
        adapterRestaurant.setOnButtonClickListener(object :
            RestaurantSearchAdapter.OnButtonClickListener {
            override fun onClick(text: String, restId: Long) {
                when (text) {
                    StatusRequest.LIST_RESTAURANTS.statusRequest -> {
                        restaurantViewModel.restaurantId.value = restId
                        findNavController().navigate(R.id.action_searchFrag_to_restaurantFrag)
                    }

                    StatusRequest.DISH.statusRequest -> {
                        // Отправка сообщения с помощью LocalBroadcastManager
                        val intent = Intent("shake_event")
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                        // А тут бэк, в котором зарандомим блюдо для своего ресторана, у которого нажмем кнопку
                        // ...
                    }
                }
            }
        })
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapterRestaurant

        adapterDish = DishSearchAdapter()
        adapterDish.setOnButtonClickListener(object : DishSearchAdapter.OnButtonClickListener {
            override fun onClick(dishesId: Long, restaurantId: Long) {
                restaurantAndDishViewModel.ids.value = arrayOf(dishesId, restaurantId)
                findNavController().navigate(R.id.action_searchFrag_to_dishFrag)
            }
        })
        binding.idListDishes.layoutManager = LinearLayoutManager(context)
        binding.idListDishes.adapter = adapterDish
    }

    private fun initNav() {
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_restaurantsFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_mainFrag)
        }
        binding.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_yandexCardFrag)
        }
        binding.idNavQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_questionsFrag)
        }
    }

    private fun initSwitch() {

        binding.idRestaurant.setOnClickListener {
            binding.idSearchView.setHint(R.string.hint_restaurant)
            searchText = ""
            binding.idSearchView.setText(searchText)
            isRestaurant = true
        }
        binding.idDish.setOnClickListener {
            binding.idSearchView.setHint(R.string.hint_dish)
            searchText = ""
            binding.idSearchView.setText(searchText)
            isRestaurant = false
        }
    }

    private fun initSearch() {
        // Инициализируем элементы управления
        // Инициализируем элементы управления
        val editText: AppCompatEditText = binding.idSearchView
        val clearButton: ImageView = binding.imIconClose

        // Добавьте обработчик текстовых изменений для AppCompatEditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Пустой метод
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                searchText = s.toString().lowercase()

                // Измените видимость ImageView в зависимости от того, пустой ли текст в AppCompatEditText
                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE

                var listRestaurants: List<RestaurantData> = ArrayList()
                var listDishes: List<MenuAndNameRestaurantData> = ArrayList()
                // Если пользователь что-то ввел в поиск (сверху)
                if (searchText.isNotEmpty()) {
                    if (isRestaurant) {

                        listRestaurants = listRestaurantsGlobal.stream().filter { restaurantData ->
                            restaurantData.restaurantName.lowercase().contains(
                                searchText
                            )
                        }.collect(Collectors.toList())

                        binding.idListRestaurants.visibility = View.VISIBLE
                        adapterRestaurant.submitList(listRestaurants)
                    } else {

                        listDishes = listDishesGlobal.stream().filter { dishData ->
                            dishData.dishName.lowercase().contains(
                                searchText
                            )
                        }.collect(Collectors.toList())

                        binding.idListDishes.visibility = View.VISIBLE
                        adapterDish.submitList(listDishes)
                    }

                    if (listRestaurants.isEmpty() && listDishes.isEmpty()) {
                        binding.tvEmptySearchResult.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptySearchResult.visibility = View.GONE
                    }

                } else {
                    binding.tvEmptySearchResult.visibility = View.GONE

                    binding.idListRestaurants.visibility = View.GONE
                    adapterRestaurant.submitList(emptyList())

                    binding.idListDishes.visibility = View.GONE
                    adapterDish.submitList(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Пустой метод
            }
        })


        // Добавляет OnClickListener для ImageView
        clearButton.setOnClickListener { // Очистите текст в AppCompatEditText
            editText.setText("")
        }
    }
}