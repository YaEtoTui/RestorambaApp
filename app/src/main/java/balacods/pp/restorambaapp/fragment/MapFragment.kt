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
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.data.module.Common
import balacods.pp.restorambaapp.data.viewModel.PointsViewModel
import balacods.pp.restorambaapp.databinding.FragmentYandexCardBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Objects.nonNull
import java.util.stream.Collectors


class MapFragment : Fragment() {

    private lateinit var binding: FragmentYandexCardBinding

    private lateinit var mapView: MapView
    private lateinit var map: Map
    private lateinit var placemarksCollection: MapObjectCollection

    private lateinit var restorambaApiService: RestorambaApiService
    private var listRestaurantsGlobalPoints: List<Point> = emptyList()

    private val pointsViewModel: PointsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(this.context)
        binding = FragmentYandexCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        mapView = binding.imCarteGeo
        map = mapView.mapWindow.map
        if (nonNull(pointsViewModel.startPoints)) {
            mapView.map.move(CameraPosition(pointsViewModel.startPoints.value!!, 13.0f, 0f, 0f))
            placemarksCollection = map.mapObjects.addCollection()
            placemarksCollection.addPlacemark(
                pointsViewModel.startPoints.value!!,
                ImageProvider.fromResource(requireContext(), R.drawable.location),
                IconStyle().apply {
                    scale = 0.6f
                    zIndex = 20f
                }
            )
        }

        showAllRestaurants()
        binding.idProgressBar.visibility = View.GONE
    }

    private fun showAllRestaurants() {
        restorambaApiService = Common.retrofitService
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
                    listRestaurantsGlobalPoints = data!!.stream().map { rest ->
                        Point(
                            rest.restaurant.restaurantCoordinateX.toDouble(),
                            rest.restaurant.restaurantCoordinateY.toDouble()
                        )
                    }.collect(Collectors.toList())


                    placemarksCollection = map.mapObjects.addCollection()

                    // Отображаем точки на карте
                    listRestaurantsGlobalPoints.forEach {
                        placemarksCollection.addPlacemark(
                            it,
                            ImageProvider.fromResource(requireContext(), R.drawable.icon_loc),
                            IconStyle().apply {
                                scale = 1.3f
                                zIndex = 20f
                            }
                        )
                    }
                } else {
                    // обработка ошибки с кодом состояния HTTP
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    private fun init() {
        binding.idProgressBar.visibility = View.VISIBLE
        initBtNav()
    }

    private fun initBtNav() {
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_mapFrag_to_restaurantsFrag)
        }
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_mapFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_mapFrag_to_mainFrag)
        }
        binding.idNavQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_mapFrag_to_questionsFrag)
        }
    }
}