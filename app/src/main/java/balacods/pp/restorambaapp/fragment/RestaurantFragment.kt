package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.FragmentRestaurantBinding
import balacods.pp.restorambaapp.fragment.adapter.DishAdapter
import balacods.pp.restorambaapp.retrofit.domain.dto.MenuData

class RestaurantFragment : Fragment() {

    private lateinit var adapter: DishAdapter
    private lateinit var binding: FragmentRestaurantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        initRcView()
        createListRestaurants() //затычка
    }

    private fun createListRestaurants() {
        val listDishes: List<MenuData> = listOf(
            MenuData(
                2,
                1,
                "Name",
                "Desc",
                "Price",
                0.3f,
                "type"
            ),
            MenuData(
                2,
                1,
                "Name",
                "Desc",
                "Price",
                0.3f,
                "type"
            ),
            MenuData(
                2,
                1,
                "Name",
                "Desc",
                "Price",
                0.3f,
                "type"
            )
        )

        adapter.submitList(listDishes)
    }

    private fun initRcView() {
        adapter = DishAdapter()
        adapter.setOnButtonClickListener(object : DishAdapter.OnButtonClickListener {
            override fun onClick() {
                findNavController().navigate(R.id.action_restaurantFrag_to_dishFrag)
            }
        })
        binding.idListDishes.layoutManager = LinearLayoutManager(context)
        binding.idListDishes.adapter = adapter
    }

    private fun onClick() {
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