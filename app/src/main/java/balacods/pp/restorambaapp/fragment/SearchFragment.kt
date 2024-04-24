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
import balacods.pp.restorambaapp.databinding.FragmentSearchBinding
import balacods.pp.restorambaapp.fragment.adapter.RestaurantSearchAdapter
import balacods.pp.restorambaapp.retrofit.domain.dto.RestaurantData
import java.util.stream.Collectors


class SearchFragment : Fragment() {

    private lateinit var adapter: RestaurantSearchAdapter
    private lateinit var binding: FragmentSearchBinding

    private var searchText: String = ""
    private val listRestaurants: List<RestaurantData> = listOf(
        RestaurantData(
            1,
            "Name 1",
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
            "Name 1",
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
            "Name 1",
            "Location",
            0.45f,
            0.45f,
            "Desc",
            "Telephone",
            0f,
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
        adapter.setOnButtonClickListener(object : RestaurantSearchAdapter.OnButtonClickListener {
            override fun onClick() {
                findNavController().navigate(R.id.action_searchFrag_to_restaurantFrag)
            }
        })
    }

    private fun init() {
        initSwitch()
        initSearch()
        initNav()
        initRcView()
    }

    private fun initRcView() {
        adapter = RestaurantSearchAdapter()
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapter
    }

    private fun initNav() {
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_mainFrag)
        }
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_searchFrag_to_restaurantsFrag)
        }
    }

    private fun initSwitch() {

        binding.idRestaurant.setOnClickListener {
            binding.idSearchView.setHint(R.string.hint_restaurant)
            searchText = ""
            binding.idSearchView.setText(searchText)
        }
        binding.idDish.setOnClickListener {
            binding.idSearchView.setHint(R.string.hint_dish)
            searchText = ""
            binding.idSearchView.setText(searchText)
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
                    binding.idListRestaurants.visibility = View.VISIBLE
                    adapter.submitList(listRestaurants.stream().filter { restaurantData ->
                        restaurantData.restaurantName.lowercase().contains(
                            searchText
                        )
                    }.collect(Collectors.toList()))
                } else {
                    binding.idListRestaurants.visibility = View.GONE
                    adapter.submitList(emptyList())
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Пустой метод
            }
        })


        // Добавьте OnClickListener для ImageView
        clearButton.setOnClickListener { // Очистите текст в AppCompatEditText
            editText.setText("")
        }
    }
}