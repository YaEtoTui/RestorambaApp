package balacods.pp.restorambaapp.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.app.OnDataPassListener
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.enum.StatusCodeShakeRequest
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.RestaurantViewModel
import balacods.pp.restorambaapp.databinding.FragmentMainBinding
import balacods.pp.restorambaapp.fragment.adapter.RestaurantSearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.stream.Collectors


class MainPageFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    // это будет именем файла настроек
    private val APP_PREFERENCES: String = "instructions"
    private var APP_PREFERENCES_INSTRUCTIONS: Boolean = false
    private var searchText: String = ""
    private lateinit var adapterRestaurant: RestaurantSearchAdapter

    private var listRestaurantsGlobal: List<RestaurantAndPhotoData> = emptyList()
    private lateinit var restorambaApiService: RestorambaApiService
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private var dataPassListener: OnDataPassListener? = null

    private var mSettings: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorambaApiService = Common.retrofitService

        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = try {
            context as OnDataPassListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPassListener")
        }
    }

    private fun init() {
        initBtNav()
        initInstructions()
        initBtFragment()
        initSearch()
        initRcView()
        initSearchListRestaurant()
    }

    private fun initBtFragment() {
        binding.idButtonGetRandomDish.setOnClickListener {
            val intent = Intent("shake_event")
            dataPassListener!!.onDataPass(StatusCodeShakeRequest.All.code)
            LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)
        }
    }

    private fun initBtNav() {
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_mainFrag_to_restaurantsFrag)
        }
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mainFrag_to_searchFrag)
        }
        binding.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_mainFrag_to_mapFrag)
        }
        binding.idNavQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_mainFrag_to_questionsFrag)
        }
    }

    private fun initRcView() {
        adapterRestaurant = RestaurantSearchAdapter()
        adapterRestaurant.setOnButtonClickListener(object :
            RestaurantSearchAdapter.OnButtonClickListener {
            override fun onClick(text: String, restaurantId: Long) {
                when (text) {
                    StatusRequest.LIST_RESTAURANTS.statusRequest -> {
                        restaurantViewModel.restaurantId.value = restaurantId
                        findNavController().navigate(R.id.action_mainFrag_to_restaurantFrag)
                    }

                    StatusRequest.DISH.statusRequest -> {
                        val intent = Intent("shake_event")
                        val code: String = String.format(
                            "%s:%s",
                            StatusCodeShakeRequest.ONLYONERESTAURANT.code,
                            restaurantId
                        )
                        dataPassListener!!.onDataPass(code)
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                    }
                }
            }
        })
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapterRestaurant
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

        binding.idMainPageFragment.setOnClickListener {
            binding.idHeader.idSearchView.visibility = View.GONE
            binding.idHeader.imSearch.visibility = View.VISIBLE
            binding.idListRestaurants.visibility = View.GONE
            binding.tvEmptySearchResult.visibility = View.GONE
            editText.setText("")
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
                    binding.idButtonGetRandomDish.visibility = View.GONE

                    val listRestaurants: List<RestaurantAndPhotoData> =
                        listRestaurantsGlobal.stream().filter { restaurantAndPhotoData ->
                            restaurantAndPhotoData.restaurant.restaurantName.lowercase().contains(
                                searchText
                            )
                        }.collect(Collectors.toList())

                    if (listRestaurants.isEmpty()) {
                        binding.tvEmptySearchResult.visibility = View.VISIBLE
                    } else {
                        binding.tvEmptySearchResult.visibility = View.GONE
                    }

                    adapterRestaurant.submitList(listRestaurants)
                } else {
                    binding.tvEmptySearchResult.visibility = View.GONE
                    binding.idListRestaurants.visibility = View.GONE
                    binding.idButtonGetRandomDish.visibility = View.VISIBLE
                    adapterRestaurant.submitList(emptyList())
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

    private fun initSearchListRestaurant() {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<List<RestaurantAndPhotoData>> = try {
                restorambaApiService.getListRestaurants()
            } catch (e: Exception) {
                return@launch
            }

            requireActivity().runOnUiThread {

                if (response.isSuccessful) {
                    val data = response.body()
                    // обработка успешного ответа
                    listRestaurantsGlobal = data!!
                } else {
                    // обработка ошибки с кодом состояния HTTP
                }
            }
        }
    }

    private fun initInstructions() {
        binding.idInstructions.idButtonAgree.setOnClickListener {
            binding.idInstructions.root.visibility = View.GONE
            binding.idNav.visibility = View.VISIBLE
            binding.idHeader.idPageHeader.visibility = View.VISIBLE
        }

        val instructions: ConstraintLayout = binding.idInstructions.root

        mSettings = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) ?: return

        if (mSettings!!.contains(APP_PREFERENCES)) {
            Log.i("contains", mSettings!!.contains(APP_PREFERENCES).toString())
            APP_PREFERENCES_INSTRUCTIONS =
                mSettings!!.getBoolean(APP_PREFERENCES, APP_PREFERENCES.toBoolean())
        }

        if (!APP_PREFERENCES_INSTRUCTIONS) {

            instructions.visibility = View.VISIBLE
            binding.idNav.visibility = View.INVISIBLE
            binding.idHeader.idPageHeader.visibility = View.INVISIBLE

            with(mSettings!!.edit()) {
                putBoolean(APP_PREFERENCES, true)
                apply()
            }
        }
    }
}