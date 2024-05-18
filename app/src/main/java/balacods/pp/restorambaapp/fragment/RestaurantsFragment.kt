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
import balacods.pp.restorambaapp.data.enum.StatusCodeShakeRequest
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.RestaurantViewModel
import balacods.pp.restorambaapp.databinding.FragmentRestaurantsBinding
import balacods.pp.restorambaapp.fragment.adapter.RestaurantAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.stream.Collectors

class RestaurantsFragment : Fragment() {

    private lateinit var adapter: RestaurantAdapter
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private lateinit var binding: FragmentRestaurantsBinding
    private lateinit var restorambaApiService: RestorambaApiService
    private var searchText: String = ""

    private var listRestaurantsGlobal: List<RestaurantData> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorambaApiService = Common.retrofitService

        init()
        searchListRestaurants()
    }

    private fun searchListRestaurants() {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<List<RestaurantData>> = restorambaApiService.getListRestaurants()
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("detail")
            }
            requireActivity().runOnUiThread {
                if (message.equals(null)) {
                    listRestaurantsGlobal = response.body()!!
                    adapter.submitList(listRestaurantsGlobal)
                }
            }
        }
    }

    private fun init() {
        initRcView()
        initBtNav()
        initSearch()
    }

    private fun initRcView() {
        adapter = RestaurantAdapter()
        adapter.setOnButtonClickListener(object : RestaurantAdapter.OnButtonClickListener {
            override fun onClick(text: String, restaurantId: Long?) {
                when (text) {
                    StatusRequest.LIST_RESTAURANTS.statusRequest -> {
                        restaurantViewModel.restaurantId.value = restaurantId
                        findNavController().navigate(R.id.action_restaurantsFrag_to_restaurantFrag)
                    }

                    StatusRequest.DISH.statusRequest -> {
                        // Отправка сообщения с помощью LocalBroadcastManager
                        val intent = Intent(StatusCodeShakeRequest.ONLYONERESTAURANT.code)
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                        // А тут бэк, в котором зарандомим блюдо для своего ресторана, у которого нажмем кнопку
                        // ...
                    }
                }
            }
        })
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapter
    }

    private fun initBtNav() {
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_mainFrag)
        }
        binding.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_mapFrag)
        }
    }

    private fun initSearch() {
        // Инициализируем элементы управления
        // Инициализируем элементы управления
        val editText: AppCompatEditText = binding.idHeader.idSearchView
        val clearButton: ImageView = binding.idHeader.imIconClose

        binding.idHeader.imSearch.setOnClickListener {
            binding.idHeader.imSearch.visibility = View.INVISIBLE
            binding.idHeader.idSearchView.visibility = View.VISIBLE
        }

        binding.idRestaurantsPage.setOnClickListener {
            binding.idHeader.idSearchView.visibility = View.GONE
            binding.idHeader.imSearch.visibility = View.VISIBLE
            binding.tvEmptySearchResult.visibility = View.GONE
            editText.setText("")
            adapter.submitList(listRestaurantsGlobal)
        }

        // Добавьте обработчик текстовых изменений для AppCompatEditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Пустой метод
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                searchText = s.toString().lowercase()

                // Измените видимость ImageView в зависимости от того, пустой ли текст в AppCompatEditText
                clearButton.visibility = if (searchText.isNotEmpty()) View.VISIBLE else View.GONE

                // Если пользователь что-то ввел в поиск (сверху)
                if (searchText.isNotEmpty()) {
                    binding.idListRestaurants.visibility = View.VISIBLE

                    val listRestaurants: List<RestaurantData> =
                        listRestaurantsGlobal.stream().filter { restaurantData ->
                            restaurantData.restaurantName.lowercase().contains(
                                searchText
                            )
                        }.collect(Collectors.toList())

                    if (listRestaurants.isEmpty()) {
                        binding.tvEmptySearchResult.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptySearchResult.visibility = View.GONE
                    }

                    adapter.submitList(listRestaurants)
                } else {
                    binding.tvEmptySearchResult.visibility = View.GONE
                    adapter.submitList(emptyList())
                }

            }

            override fun afterTextChanged(s: Editable) {
                // Пустой метод
            }
        })


        // Добавляет OnClickListener для ImageView
        clearButton.setOnClickListener { // Очистите текст в AppCompatEditText
            editText.setText("")
            adapter.submitList(listRestaurantsGlobal)
        }
    }
}