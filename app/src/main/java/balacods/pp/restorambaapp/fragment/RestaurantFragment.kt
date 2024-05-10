package balacods.pp.restorambaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.databinding.FragmentRestaurantBinding
import balacods.pp.restorambaapp.fragment.adapter.DishAdapter

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

        init()
    }

    private fun init() {
        initBtNav()
        initRcView()
        createListDishes() //затычка
        initBtPage()
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

    private fun createListDishes() {
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