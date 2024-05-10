package balacods.pp.restorambaapp.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.data.enum.StatusRequest
import balacods.pp.restorambaapp.data.model.RestaurantData
import balacods.pp.restorambaapp.databinding.ItemListRestaurantsSearchWhiteGreenBinding

class RestaurantSearchAdapter :
    ListAdapter<RestaurantData, RestaurantSearchAdapter.Holder>(Comparator()) {

    private lateinit var onButtonClickListener: RestaurantSearchAdapter.OnButtonClickListener

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemListRestaurantsSearchWhiteGreenBinding.bind(view)

        fun bind(
            restaurantData: RestaurantData,
            onButtonClickListener: RestaurantSearchAdapter.OnButtonClickListener
        ) = with(binding) {

            binding.tvTitleRestaurant.text = restaurantData.restaurantName

            binding.cView.setOnClickListener {
                onButtonClickListener.onClick(StatusRequest.LIST_RESTAURANTS.statusRequest)
            }

            binding.idBtRandom.setOnClickListener {
                onButtonClickListener.onClick(StatusRequest.DISH.statusRequest)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<RestaurantData>() {
        override fun areItemsTheSame(oldItem: RestaurantData, newItem: RestaurantData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RestaurantData, newItem: RestaurantData): Boolean {
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
        fun onClick(text: String)
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        onButtonClickListener = listener
    }
}