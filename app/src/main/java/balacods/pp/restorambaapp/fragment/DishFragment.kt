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
import balacods.pp.restorambaapp.data.model.DishAndPhotoData
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.RestaurantAndDishViewModel
import balacods.pp.restorambaapp.databinding.FragmentDishBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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

        binding.idProgressBar.visibility = View.VISIBLE
        binding.idScroll.visibility = View.INVISIBLE
        onClick()
        initRetrofitData()
    }

    private fun initRetrofitData() {
        restaurantAndDishViewModel.ids.observe(viewLifecycleOwner) { ids ->
            CoroutineScope(Dispatchers.IO).launch {
                val responseMenuData: Response<DishAndPhotoData> =
                    restorambaApiService.getDishByRestaurantAndDishId(ids[1], ids[0])
                val messageMenuData = responseMenuData.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseRestaurant: Response<RestaurantAndPhotoData> =
                    restorambaApiService.getRestaurantById(ids[1])
                val messageRestaurant = responseRestaurant.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                requireActivity().runOnUiThread {
                    if (messageMenuData.equals(null) && messageRestaurant.equals(null)) {
                        val menuData: DishAndPhotoData = responseMenuData.body()!!
                        val restaurantData: RestaurantAndPhotoData = responseRestaurant.body()!!
                        binding.apply {

                            if (menuData.photo != null) {
                                imIconPhoto.visibility = View.GONE
                                Glide.with(requireContext())
                                    .load(menuData.photo.link1)
                                    .centerInside()
                                    .transform(RoundedCorners(20))
                                    .error(R.drawable.ic_launcher_foreground)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(idRectanglePhoto)
                            }

                            idNameDish.text = menuData.dish.dishName
                            idNameRestaurant.text = restaurantData.restaurant.restaurantName
                            tvDesc.text = menuData.dish.dishDescription
                            tvGr.text = String.format("%s гр", menuData.dish.dishWeight.toInt())
                            tvRub.text = String.format("%s руб", menuData.dish.dishPrice)
                        }

                        binding.idProgressBar.visibility = View.GONE
                        binding.idScroll.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun onClick() {
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_restaurantsFrag)
        }
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_mainFrag)
        }
        binding.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_mapFrag)
        }
        binding.idNavQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_dishFrag_to_questionsFrag)
        }
    }
}