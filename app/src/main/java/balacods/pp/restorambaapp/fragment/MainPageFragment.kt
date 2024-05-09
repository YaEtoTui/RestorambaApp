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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.databinding.FragmentMainBinding
import balacods.pp.restorambaapp.fragment.adapter.RestaurantSearchAdapter
import java.util.stream.Collectors


class MainPageFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    // это будет именем файла настроек
    private val APP_PREFERENCES: String = "instructions"
    private var APP_PREFERENCES_INSTRUCTIONS: Boolean = false
    private var searchText: String = ""
    private lateinit var adapterRestaurant: RestaurantSearchAdapter

    private val listRestaurantsGlobal: List<RestaurantData> = listOf(
        RestaurantData(
            1,
            "Restaurant 1",
            "Location",
            0.45f,
            0.45f,
            "Desc",
            "Telephone",
            0f,
            "type"
        ),
        RestaurantData(
            1,
            "Restaurant 2",
            "Location",
            0.45f,
            0.45f,
            "Desc",
            "Telephone",
            0f,
            "type"
        ),
        RestaurantData(
            1,
            "Restaurant 3",
            "Location",
            0.45f,
            0.45f,
            "Desc",
            "Telephone",
            0f,
            "type"
        )
    )

    var mSettings: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initBtNav()
        initInstructions()
        initBtFragment()
        initSearch()
        initRcView()
    }

    private fun initBtFragment() {
        binding.idButtonGetRandomDish.setOnClickListener {

            // Отправка сообщения с помощью LocalBroadcastManager
            val intent = Intent("shake_event")
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
            findNavController().navigate(R.id.action_mainFrag_to_yandexCardFrag)
        }
    }

    private fun initRcView() {
        adapterRestaurant = RestaurantSearchAdapter()
        adapterRestaurant.setOnButtonClickListener(object :
            RestaurantSearchAdapter.OnButtonClickListener {
            override fun onClick() {
                findNavController().navigate(R.id.action_mainFrag_to_restaurantFrag)
            }
        })
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapterRestaurant
    }

    private fun initSearch() {

        // Инициализируйте ваши элементы управления
        // Инициализируйте ваши элементы управления
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