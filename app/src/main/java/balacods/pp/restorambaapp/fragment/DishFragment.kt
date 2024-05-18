package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.model.MenuData
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.RestaurantAndDishViewModel
import balacods.pp.restorambaapp.databinding.FragmentDishBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class DishFragment : Fragment() {

    private lateinit var binding: FragmentDishBinding

    private lateinit var restorambaApiService: RestorambaApiService
    private val restaurantAndDishViewModel: RestaurantAndDishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorambaApiService = Common.retrofitService

        onClick()
        initRetrofitData()
    }

    private fun initRetrofitData() {
        restaurantAndDishViewModel.ids.observe(viewLifecycleOwner) { ids ->
            CoroutineScope(Dispatchers.IO).launch {
                val responseMenuData: Response<List<MenuData>> = restorambaApiService.getDishByRestaurantAndDishId(ids[0], ids[1])
                val messageMenuData = responseMenuData.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseRestaurant: Response<List<RestaurantData>> = restorambaApiService.getRestaurantById(ids[1])
                val messageRestaurant = responseRestaurant.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                requireActivity().runOnUiThread {
                    if (messageMenuData.equals(null) && messageRestaurant.equals(null)) {
                        val menuData: MenuData = responseMenuData.body()!![0]
                        val restaurantData: RestaurantData = responseRestaurant.body()!![0]
                        binding.apply {
                            idNameDish.text = menuData.dishName
                            idNameRestaurant.text = restaurantData.restaurantName
                            tvDesc.text = menuData.dishDescription
                            tvGr.text = String.format("%s гр", menuData.dishWeight.toInt())
                            tvRub.text = String.format("%s руб", menuData.dishPrice)
                        }
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_mainFrag)
        }
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_restaurantsFrag)
        }
    }
}