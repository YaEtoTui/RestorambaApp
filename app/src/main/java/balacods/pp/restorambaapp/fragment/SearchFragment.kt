package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.databinding.FragmentSearchBinding
import balacods.pp.restorambaapp.fragment.adapter.DishSearchAdapter
import balacods.pp.restorambaapp.fragment.adapter.RestaurantSearchAdapter
import java.util.stream.Collectors


class SearchFragment : Fragment() {

    private lateinit var adapterRestaurant: RestaurantSearchAdapter
    private lateinit var adapterDish: DishSearchAdapter
    private lateinit var binding: FragmentSearchBinding

    private var searchText: String = ""
    private var isRestaurant: Boolean = true
    private val listRestaurants: List<RestaurantData> = listOf(
        RestaurantData(
            1,
            "Ресторан 1",
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
            "Ресторан 2",
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
            "Ресторан 3",
            "Location",
            0.45f,
            0.45f,
            "Desc",
            "Telephone",
            0f,
            "type"
        )
    )
    private val listDishes: List<MenuData> = listOf(
        MenuData(
            2,
            1,
            "Блюдо 1",
            "Desc",
            "Price",
            0.3f,
            "type"
        ),
        MenuData(
            2,
            1,
            "Блюдо 2",
            "Desc",
            "Price",
            0.3f,
            "type"
        ),
        MenuData(
            2,
            1,
            "Блюдо 3",
            "Desc",
            "Price",
            0.3f,
            "type"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initSwitch()
        initSearch()
        initNav()
        initRcView()
    }

    private fun initRcView() {
        adapterRestaurant = RestaurantSearchAdapter()
        adapterRestaurant.setOnButtonClickListener(object :
            RestaurantSearchAdapter.OnButtonClickListener {
            override fun onClick() {
                findNavController().navigate(R.id.action_searchFrag_to_restaurantFrag)
            }
        })
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapterRestaurant

        adapterDish = DishSearchAdapter()
        adapterDish.setOnButtonClickListener(object : DishSearchAdapter.OnButtonClickListener {
            override fun onClick() {
                findNavController().navigate(R.id.action_searchFrag_to_dishFrag)
            }
        })
        binding.idListDishes.layoutManager = LinearLayoutManager(context)
        binding.idListDishes.adapter = adapterDish
    }

    private fun initNav() {
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_mainFrag)
        }
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_restaurantsFrag)
        }
        binding.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_yandexCardFrag)
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
        // Инициализируйте ваши элементы управления
        // Инициализируйте ваши элементы управления
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



                if (searchText.isNotEmpty()) {
                    if (isRestaurant) {
                        binding.idListRestaurants.visibility = View.VISIBLE
                        adapterRestaurant.submitList(
                            listRestaurants.stream().filter { restaurantData ->
                                restaurantData.restaurantName.lowercase().contains(
                                    searchText
                                )
                            }.collect(Collectors.toList())
                        )
                    } else {
                        binding.idListDishes.visibility = View.VISIBLE
                        adapterDish.submitList(listDishes.stream().filter { dishData ->
                            dishData.dishName.lowercase().contains(
                                searchText
                            )
                        }.collect(Collectors.toList()))
                    }

                } else {
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