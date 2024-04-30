package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.FragmentRestaurantsBinding
import balacods.pp.restorambaapp.fragment.adapter.RestaurantAdapter
import balacods.pp.restorambaapp.data.model.RestaurantData

class RestaurantsFragment : Fragment() {

    private lateinit var adapter: RestaurantAdapter
    private lateinit var binding: FragmentRestaurantsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRcView()
        createListRestaurants() //заглушка
        onClick()
    }

    private fun createListRestaurants() {
        val listRestaurants: List<RestaurantData> = listOf(
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

        adapter.submitList(listRestaurants)
    }

    private fun initRcView() {
        adapter = RestaurantAdapter()
        adapter.setOnButtonClickListener(object: RestaurantAdapter.OnButtonClickListener {
            override fun onClick() {
                    findNavController().navigate(R.id.action_restaurantsFrag_to_restaurantFrag)
            }
        })
        binding.idListRestaurants.layoutManager = LinearLayoutManager(context)
        binding.idListRestaurants.adapter = adapter
    }

    private fun onClick() {
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_mainFrag)
        }
//        binding.idNavMap.setOnClickListener {
//            findNavController().navigate(R.id.action_restaurantsFrag_to_yandexCardFrag)
//        }
    }
}