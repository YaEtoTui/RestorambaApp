package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoDTO
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.data.viewModel.PointsViewModel
import balacods.pp.restorambaapp.databinding.ItemListRestaurantsBinding
import balacods.pp.restorambaapp.distance.findShortestDistance
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yandex.mapkit.geometry.Point

class RestaurantAdapter :
    ListAdapter<RestaurantAndPhotoDTO, RestaurantAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: RestaurantAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListRestaurantsBinding.bind(view)

        fun bind(
            restaurantData: RestaurantAndPhotoDTO,
            onButtonClickListener: RestaurantAdapter.OnButtonClickListener
        ) = with(binding) {

            val pointRestaurant: Point = Point(restaurantData.restaurant.restaurantCoordinateX.toDouble(),
                restaurantData.restaurant.restaurantCoordinateX.toDouble()
            )

            tvPath.text = String.format("Расстояние до вас: %s", findShortestDistance(restaurantData.point, pointRestaurant))

            if (restaurantData.photo != null) {
                imPhotoIcon.visibility = View.GONE
                Glide.with(itemView.context)
                    .load(restaurantData.photo.link1)
                    .centerInside()
                    .transform(RoundedCorners(45))
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imPhoto)
            }

            binding.tvTitleRestaurant.text = restaurantData.restaurant.restaurantName

            binding.cView.setOnClickListener {
                onButtonClickListener.onClick(
                    StatusRequest.LIST_RESTAURANTS.statusRequest,
                    restaurantData.restaurant.customerId,
                    Point(restaurantData.restaurant.restaurantCoordinateX.toDouble(),
                        restaurantData.restaurant.restaurantCoordinateY.toDouble()
                    )
                )
            }

            binding.idBtRandom.setOnClickListener {
                onButtonClickListener.onClick(
                    StatusRequest.DISH.statusRequest,
                    restaurantData.restaurant.customerId,
                    Point(restaurantData.restaurant.restaurantCoordinateX.toDouble(),
                        restaurantData.restaurant.restaurantCoordinateY.toDouble()
                    )
                )
            }

            binding.idBtMap.setOnClickListener {
                onButtonClickListener.onClick(
                    StatusRequest.MAP_DISTANCE.statusRequest,
                    restaurantData.restaurant.customerId,
                    Point(restaurantData.restaurant.restaurantCoordinateX.toDouble(),
                        restaurantData.restaurant.restaurantCoordinateY.toDouble()
                    )
                )
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<RestaurantAndPhotoDTO>() {
        override fun areItemsTheSame(
            oldItem: RestaurantAndPhotoDTO,
            newItem: RestaurantAndPhotoDTO
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RestaurantAndPhotoDTO,
            newItem: RestaurantAndPhotoDTO
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_restaurants, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }

    interface OnButtonClickListener {
        fun onClick(text: String, restaurantId: Long?, point: Point)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}