package balacods.pp.restorambaapp.fragment
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.FragmentYandexCardBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CompositeIcon
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment(), UserLocationObjectListener {

    private lateinit var binding: FragmentYandexCardBinding

    private lateinit var mapView: MapView
    private lateinit var map: Map
    private lateinit var point: Point
    private lateinit var userLocationLayer: UserLocationLayer
    private val PERMISSIONS_REQUEST_FINE_LOCATION = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(this.context)
        binding = FragmentYandexCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                "android.permission.ACCESS_FINE_LOCATION"
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                PERMISSIONS_REQUEST_FINE_LOCATION
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        mapView = binding.imCarteGeo
//        map = mapView.mapWindow.map

        mapView.map.isRotateGesturesEnabled = false
        mapView.map.move(CameraPosition(Point(0.0, 0.0), 14f, 0f, 0f))

        requestLocationPermission()

        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true

        userLocationLayer.setObjectListener(this)

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
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        userLocationView.getArrow().setIcon(
            ImageProvider.fromResource(
                context, R.drawable.icon_loc
            )
        )

        val pinIcon: CompositeIcon = userLocationView.getPin().useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(context, R.drawable.icon_loc),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(context, R.drawable.icon_loc),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001

    }

    override fun onObjectRemoved(userLocationView: UserLocationView) {
        TODO("Not yet implemented")
    }

    override fun onObjectUpdated(userLocationView: UserLocationView, p1: ObjectEvent) {
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )

        userLocationView.getArrow().setIcon(
            ImageProvider.fromResource(
                context, R.drawable.icon_loc
            )
        )

        val pinIcon: CompositeIcon = userLocationView.getPin().useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(context, R.drawable.icon_loc),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(context, R.drawable.icon_loc),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }
}