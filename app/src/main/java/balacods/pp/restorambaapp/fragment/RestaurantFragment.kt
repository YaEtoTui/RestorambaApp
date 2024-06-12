package balacods.pp.restorambaapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.app.OnDataPassListener
import balacods.pp.restorambaapp.data.api.retrofit.RestorambaApiService
import balacods.pp.restorambaapp.data.enum.StatusCodeShakeRequest
import balacods.pp.restorambaapp.data.model.DishAndPhotoData
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.PointsViewModel
import balacods.pp.restorambaapp.data.viewModel.RestaurantAndDishViewModel
import balacods.pp.restorambaapp.data.viewModel.RestaurantViewModel
import balacods.pp.restorambaapp.databinding.FragmentRestaurantBinding
import balacods.pp.restorambaapp.fragment.adapter.DishAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class RestaurantFragment : Fragment() {

    private lateinit var adapter: DishAdapter
    private lateinit var binding: FragmentRestaurantBinding
    private val pointsViewModel: PointsViewModel by activityViewModels()

    private lateinit var restorambaApiService: RestorambaApiService
    private val restaurantViewModel: RestaurantViewModel by activityViewModels()
    private val restaurantAndDishViewModel: RestaurantAndDishViewModel by activityViewModels()
    private var dataPassListener: OnDataPassListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restorambaApiService = Common.retrofitService

        binding.idScroll.visibility = View.INVISIBLE
        binding.idProgressBar.visibility = View.VISIBLE
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
        initRetrofitData()
        initBtNav()
        initRcView()
    }

    private fun initRetrofitData() {
        restaurantViewModel.restaurantId.observe(viewLifecycleOwner) { restaurantId ->
            CoroutineScope(Dispatchers.IO).launch {
                val responseRestaurant: Response<RestaurantAndPhotoData> =
                    restorambaApiService.getRestaurantById(restaurantId)
                val messageRestaurant = responseRestaurant.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                val responseMenu: Response<List<DishAndPhotoData>> =
                    restorambaApiService.getDishesByRestaurantId(restaurantId)
                val messageMenu = responseMenu.errorBody()?.string()?.let {
                    JSONObject(it).getString("detail")
                }
                requireActivity().runOnUiThread {
                    if (messageRestaurant.equals(null)) {
                        val restaurantData: RestaurantAndPhotoData = responseRestaurant.body()!!
                        binding.apply {

                            if (restaurantData.photo != null) {
                                imIconPhoto.visibility = View.GONE
                                Glide.with(requireContext())
                                    .load(restaurantData.photo.link1)
                                    .centerInside()
                                    .transform(RoundedCorners(45))
                                    .error(R.drawable.ic_launcher_foreground)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(idRectanglePhoto)
                            }

                            binding.idBtMap.setOnClickListener {
                                pointsViewModel.endPoint.value = Point(
                                    restaurantData.restaurant.restaurantCoordinateX.toDouble(),
                                    restaurantData.restaurant.restaurantCoordinateY.toDouble()
                                )
                                findNavController().navigate(R.id.action_restaurantFrag_to_yandexCardFrag)
                            }

                            idNameRestaurant.text = restaurantData.restaurant.restaurantName
                            tvDesc.text = restaurantData.restaurant.restaurantDescription
                            idButtonGetRandomDish.setOnClickListener {
                                val intent = Intent("shake_event")
                                val code: String = String.format(
                                    "%s:%s",
                                    StatusCodeShakeRequest.ONLYONERESTAURANT.code,
                                    restaurantId
                                )
                                dataPassListener!!.onDataPass(code)
                                LocalBroadcastManager.getInstance(requireContext())
                                    .sendBroadcast(intent)
                            }
                        }
                    }
                    if (messageMenu.equals(null)) {
                        val menuDataList: List<DishAndPhotoData> = responseMenu.body()!!
                        adapter.submitList(menuDataList)
                    }

                    binding.idScroll.visibility = View.VISIBLE
                    binding.idProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun initRcView() {
        adapter = DishAdapter()
        adapter.setOnButtonClickListener(object : DishAdapter.OnButtonClickListener {
            override fun onClick(dishId: Long, restaurantId: Long) {
                restaurantAndDishViewModel.ids.value = arrayOf(dishId, restaurantId)
                findNavController().navigate(R.id.action_restaurantFrag_to_dishFrag)
            }
        })
        binding.idListDishes.layoutManager = LinearLayoutManager(context)
        binding.idListDishes.adapter = adapter
    }

    private fun initBtNav() {
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_restaurantsFrag)
        }
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_mainFrag)
        }
        binding.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_mapFrag)
        }
        binding.idNavQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_questionsFrag)
        }
    }
}