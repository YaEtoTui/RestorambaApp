package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.RestaurantAndPhotoData
import balacods.pp.restorambaapp.databinding.ItemListRestaurantsSearchWhiteGreenBinding
import balacods.pp.restorambaapp.distance.findShortestDistance
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yandex.mapkit.geometry.Point

class RestaurantSearchAdapter :
    ListAdapter<RestaurantAndPhotoData, RestaurantSearchAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: RestaurantSearchAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListRestaurantsSearchWhiteGreenBinding.bind(view)

        fun bind(
            restaurantData: RestaurantAndPhotoData,
            onButtonClickListener: RestaurantSearchAdapter.OnButtonClickListener
        ) = with(binding) {

            val pointRestaurant: Point = Point(restaurantData.restaurant.restaurantCoordinateX.toDouble(),
                restaurantData.restaurant.restaurantCoordinateX.toDouble()
            )

            tvPath.text = String.format("Расстояние до вас: %s", findShortestDistance(Point(56.840823, 60.650763), pointRestaurant))

            if (restaurantData.photo != null) {
                imPhotoIcon.visibility = View.GONE
                Glide.with(itemView.context)
                    .load(restaurantData.photo.link1)
                    .centerInside()
                    .transform(RoundedCorners(20))
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imPhoto)
            }

            binding.tvTitleRestaurant.text = restaurantData.restaurant.restaurantName

            binding.cView.setOnClickListener {
                onButtonClickListener.onClick(StatusRequest.LIST_RESTAURANTS.statusRequest, restaurantData.restaurant.customerId)
            }

            binding.idBtRandom.setOnClickListener {
                onButtonClickListener.onClick(StatusRequest.DISH.statusRequest, restaurantData.restaurant.customerId)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<RestaurantAndPhotoData>() {
        override fun areItemsTheSame(oldItem: RestaurantAndPhotoData, newItem: RestaurantAndPhotoData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RestaurantAndPhotoData, newItem: RestaurantAndPhotoData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_restaurants_search_white_green, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onButtonClickListener)
    }

    interface OnButtonClickListener {
        fun onClick(text: String, restId: Long)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}