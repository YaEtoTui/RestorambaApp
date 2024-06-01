package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.app.common.CommonColors
import balacods.pp.restorambaapp.app.common.showToast
import balacods.pp.restorambaapp.data.viewModel.PointsViewModel
import balacods.pp.restorambaapp.databinding.FragmentYandexCardBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError

class YandexCardFragment : Fragment() {

    private lateinit var binding: FragmentYandexCardBinding
    private val pointsViewModel: PointsViewModel by activityViewModels()

    private lateinit var mapView: MapView
    private lateinit var map: Map

    private lateinit var drivingRouter: DrivingRouter
    private var drivingSession: DrivingSession? = null
    private lateinit var placemarksCollection: MapObjectCollection
    private lateinit var routesCollection: MapObjectCollection

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) = Unit

        override fun onMapLongTap(map: Map, point: Point) {
            routePoints = routePoints + point
        }
    }

    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            routes = drivingRoutes
        }

        override fun onDrivingRoutesError(error: Error) {
            when (error) {
                is NetworkError -> context!!.showToast("Routes request error due network issues")
                else -> context!!.showToast("Routes request unknown error")
            }
        }
    }

    private var routePoints = emptyList<Point>()
        set(value) {
            field = value
            onRoutePointsUpdated()
        }

    private var routes = emptyList<DrivingRoute>()
        set(value) {
            field = value
            onRoutesUpdated()
        }

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
        map.addInputListener(inputListener)

        // код - 1, значит показать кратчайший путь
        if (pointsViewModel.code.value == 1) {
            pointsViewModel.code.value = 0
            showDistance()
        } else {
            val map = mapView.mapWindow.map
            map.move(CameraPosition(Point(56.840823, 60.650763), 13.0f, 0f, 0f))
//            showAllRestaurants()
        }
    }

//    private fun showAllRestaurants() {
//        TODO("Not yet implemented")
//    }

    private fun showDistance() {

        placemarksCollection = map.mapObjects.addCollection()
        routesCollection = map.mapObjects.addCollection()

        drivingRouter =
            DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)

        val map = mapView.mapWindow.map
        map.move(CameraPosition(pointsViewModel.startPoints.value!!, 13.0f, 0f, 0f))

        routePoints = pointsViewModel.allPoints.value!!
    }

    private fun init() {
        initBtNav()
    }

    private fun initBtNav() {
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_yandexCardFrag_to_restaurantsFrag)
        }
        binding.idNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_yandexCardFrag_to_searchFrag)
        }
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_yandexCardFrag_to_mainFrag)
        }
        binding.idNavQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_yandexCardFrag_to_questionsFrag)
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    private fun onRoutePointsUpdated() {
        placemarksCollection.clear()

        if (routePoints.isEmpty()) {
            drivingSession?.cancel()
            routes = emptyList()
            return
        }

        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.location)
        routePoints.forEach {
            placemarksCollection.addPlacemark(
                it,
                imageProvider,
                IconStyle().apply {
                    scale = 0.6f
                    zIndex = 20f
                }
            )
        }

        if (routePoints.size < 2) return

        val requestPoints = buildList {
            add(RequestPoint(routePoints.first(), RequestPointType.WAYPOINT, null, null))
            addAll(
                routePoints.subList(1, routePoints.size - 1)
                    .map { RequestPoint(it, RequestPointType.VIAPOINT, null, null) })
            add(RequestPoint(routePoints.last(), RequestPointType.WAYPOINT, null, null))
        }

        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingSession = drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            drivingRouteListener,
        )
    }

    private fun onRoutesUpdated() {
        routesCollection.clear()
        if (routes.isEmpty()) return

        routes.forEachIndexed { index, route ->
            routesCollection.addPolyline(route.geometry).apply {
                if (index == 0) {
                    styleMainRoute() /*else styleAlternativeRoute()*/
                    return
                }
            }
        }
    }

    private fun PolylineMapObject.styleMainRoute() {
        zIndex = 10f
        setStrokeColor(ContextCompat.getColor(requireContext(), CommonColors.green_path))
        strokeWidth = 3f
        outlineColor = ContextCompat.getColor(requireContext(), CommonColors.black)
        outlineWidth = 0.2f
    }


    companion object {

        private val START_POSITION = CameraPosition(Point(56.840823, 60.650763), 13.0f, 0f, 0f)

        private val DEFAULT_POINTS = emptyList<Point>()
    }
}